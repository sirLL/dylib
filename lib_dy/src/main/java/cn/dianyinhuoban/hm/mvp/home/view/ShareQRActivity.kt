package cn.dianyinhuoban.hm.mvp.home.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.api.URLConfig
import cn.dianyinhuoban.hm.mvp.bean.ShareItemBean
import cn.dianyinhuoban.hm.mvp.home.GalleryItemDecoration
import cn.dianyinhuoban.hm.mvp.home.contract.ShareContract
import cn.dianyinhuoban.hm.mvp.home.presenter.SharePresenter
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.BitmapUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.activity_share_qr.*
import kotlinx.android.synthetic.main.item_share.view.*
import java.io.File
import java.util.*

class ShareQRActivity : BaseActivity<SharePresenter?>(), ShareContract.View {

    private var mAdapter: SimpleAdapter<ShareItemBean>? = null
    private var mBitmap: Bitmap? = null

    companion object {
        const val ACTION_SAVE_IMG = 10
        const val ACTION_SHARE_IMG = 11
    }

    override fun getPresenter(): SharePresenter? {
        return SharePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_qr)
        setTitle(
            "二维码分享",
            ContextCompat.getColor(ShareQRActivity@ this, R.color.dy_color_base_page_title)
        )
        val qrContent = URLConfig.PAGE_WEB_REGISTER+MMKVUtil.getInviteCode()
        createQR(qrContent)
        createAdapter()
        recycler_view.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(GalleryItemDecoration())
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recycler_view)
        mPresenter?.fetchShareImage()
    }

    private fun createQR(qrContent: String) {
        Observable.just(qrContent)
            .map { s: String? ->
                QRCodeEncoder.syncEncodeQRCode(
                    s,
                    400,
                    Color.parseColor("#000000")
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Bitmap> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(bitmap: Bitmap) {
                    mBitmap = bitmap
                    mAdapter?.notifyDataSetChanged()
                }

                override fun onError(e: Throwable) {
                    showToast("生成二维码失败")
                }

                override fun onComplete() {}
            })
    }

    private fun createAdapter() {
        mAdapter = object : SimpleAdapter<ShareItemBean>(R.layout.item_share) {
            override fun convert(
                viewHolder: SimpleViewHolder?,
                position: Int,
                itemData: ShareItemBean?
            ) {
                Glide.with(this@ShareQRActivity).load(itemData?.thumb ?: "").asBitmap()
                    .into(object :SimpleTarget<Bitmap>(){
                        override fun onResourceReady(
                            resource: Bitmap?,
                            glideAnimation: GlideAnimation<in Bitmap>?
                        ) {
                            viewHolder?.itemView?.iv_cover?.setImageBitmap(resource)
                        }

                    })
//                viewHolder?.itemView?.iv_cover?.load(itemData?.thumb ?: "") {
//                    crossfade(true)
//                }
                val name = if (!TextUtils.isEmpty(MMKVUtil.getNick())) {
                    MMKVUtil.getNick()
                } else {
                    MMKVUtil.getUserName()
                }
                viewHolder?.itemView?.tv_name?.text = "姓名:${name ?: ""}"
                viewHolder?.itemView?.tv_invite_code?.text = "推荐码:${MMKVUtil.getInviteCode()}"
                viewHolder?.itemView?.iv_qr?.setImageBitmap(mBitmap)
                viewHolder?.itemView?.tv_save?.setOnClickListener {
                    saveView(viewHolder?.itemView?.share_container, ACTION_SAVE_IMG)
                }
                viewHolder?.itemView?.tv_share?.setOnClickListener {
                    saveView(viewHolder?.itemView?.share_container, ACTION_SHARE_IMG)
                }
            }

            override fun onItemClick(data: ShareItemBean?, position: Int) {

            }

        }
    }

    override fun bindShareImage(data: List<ShareItemBean>?) {
        mAdapter?.data = data
        Handler().postDelayed({
            mAdapter?.data?.let {
                if (it.size > 0) {
                    recycler_view.scrollToPosition(0)
                }
            }
        }, 100)
    }


    private fun saveView(view: View, action: Int) {
        RxPermissions(ShareQRActivity@ this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { aBoolean: Boolean ->
            if (aBoolean) {
                saveView2File(view, action)
            } else {
                showToast("你尚未开启读写权限")
            }
        }
    }

    private fun saveView2File(view: View, action: Int) {
        Observable.just(view)
            .map { v ->
                val fileName = "DYHM${Calendar.getInstance().timeInMillis}.jpg"
                BitmapUtils.saveBitmap(
                    ShareQRActivity@ this,
                    BitmapUtils.view2Bitmap(v),
                    fileName
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(path: String) {
                    if (action == ACTION_SAVE_IMG) {
                        showToast("图片保存至${path}")
                    } else {
                        shareFile(this@ShareQRActivity, path)
                    }
                }

                override fun onError(e: Throwable) {
                    showToast("图片保存失败")
                }

                override fun onComplete() {}
            })
    }

    private fun shareFile(context: Context, filePath: String) {
        val file = File(filePath)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val uri = Uri.fromFile(file)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(Intent.createChooser(shareIntent, "share to"));
    }
}