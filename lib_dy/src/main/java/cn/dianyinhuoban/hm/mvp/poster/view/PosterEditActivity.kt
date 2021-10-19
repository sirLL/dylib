package cn.dianyinhuoban.hm.mvp.poster.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.text.TextUtils
import android.view.View
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.api.URLConfig
import cn.dianyinhuoban.hm.mvp.bean.PosterItemBean
import cn.dianyinhuoban.hm.util.StringUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gyf.immersionbar.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.BitmapUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dy_activity_poster_edit.*
import java.io.File
import java.util.*

class PosterEditActivity : BaseActivity<IPresenter?>() {
    private var mPoster: PosterItemBean? = null

    companion object {
        const val ACTION_SAVE_IMG = 10
        const val ACTION_SHARE_IMG = 11

        fun openPosterEditActivity(context: Context, posterBean: PosterItemBean) {
            val intent = Intent(context, PosterEditActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("poster", posterBean)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun isDarkModeEnable(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return R.color.colorPrimary
    }

    override fun getToolbarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun getBackButtonIcon(): Int {
        return R.drawable.dy_ic_back_circle
    }

    override fun getRootView(): Int {
        return R.layout.dy_activity_poster_edit_root
    }

    override fun initStatusBar() {

    }

    private fun setupStatusBar() {
        ImmersionBar.with(this)
            .autoDarkModeEnable(isDarkModeEnable)
            .autoStatusBarDarkModeEnable(isDarkModeEnable)
            .statusBarColor(statusBarColor)
            .fitsSystemWindows(true)
            .flymeOSStatusBarFontColor(statusBarColor)
            .titleBarMarginTop(mToolbar)
            .init()
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mPoster = bundle?.getParcelable("poster")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_poster_edit)
        setupStatusBar()

        cb_show_qr.setOnCheckedChangeListener { _, isChecked ->
            cl_info_container.visibility = if (isChecked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        tv_save.setOnClickListener {
            saveView(cl_share_container, ACTION_SAVE_IMG)
        }
        tv_share.setOnClickListener {
            saveView(cl_share_container, ACTION_SHARE_IMG)
        }
        tv_copy.setOnClickListener {
            val content = ed_content.text.toString()
            if (content.isNotEmpty()) {
                StringUtil.copyString(PosterEditActivity@ this, content)
                showToast("复制成功")
            }
        }
        mPoster?.description?.let {
            if (it.isNotEmpty()) {
                ed_content.setText(it)
                ed_content.setSelection(it.length)
            }
        }
        Glide.with(this)
            .asBitmap()
            .load(mPoster?.thumb ?: "")
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    iv_cover.setImageBitmap(resource)
                }
            })


        val qrContent = URLConfig.PAGE_WEB_REGISTER + MMKVUtil.getInviteCode()
        createQR(qrContent)

        val name = if (!TextUtils.isEmpty(MMKVUtil.getNick())) {
            MMKVUtil.getNick()
        } else {
            MMKVUtil.getUserName()
        }
        tv_name.text = "姓名:${name}"
        tv_invite_code.text = "推荐码:${MMKVUtil.getInviteCode()}"
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    private fun createQR(qrContent: String) {
        Observable.just(qrContent)
            .map { s: String? ->
                val bitmap: Bitmap = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.dy_ic_app_logo
                )
                QRCodeEncoder.syncEncodeQRCode(
                    s,
                    400,
                    Color.parseColor("#000000"),
                    bitmap
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Bitmap> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(bitmap: Bitmap) {
                    iv_qr.setImageBitmap(bitmap)
                }

                override fun onError(e: Throwable) {
                    showToast("生成二维码失败")
                }

                override fun onComplete() {}
            })
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
        showLoading()
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
                    dismissProgress()
                    if (action == ACTION_SAVE_IMG) {
                        showToast("图片保存至${path}")
                    } else {
                        shareFile(this@PosterEditActivity, path)
                    }
                }

                override fun onError(e: Throwable) {
                    showToast("图片保存失败")
                    dismissProgress()
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "分享内容")
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)));
    }
}