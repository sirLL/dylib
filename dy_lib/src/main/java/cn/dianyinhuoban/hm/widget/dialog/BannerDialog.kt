package cn.dianyinhuoban.hm.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.WebActivity
import cn.dianyinhuoban.hm.mvp.bean.BannerBean
import cn.dianyinhuoban.hm.util.ToolUtil
import coil.load
import kotlinx.android.synthetic.main.dialog_home_banner.*

class BannerDialog(context: Context) : Dialog(context, R.style.MessageDialog) {

    private var mCurrentIndex: Int = 0

    private val mData: MutableList<BannerBean> by lazy {
        mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_home_banner)

        iv_cancel.setOnClickListener {
            dismiss()
        }

        tv_next.setOnClickListener {
            dismiss()
            Handler().postDelayed({
                show()
                showNextImage()
            }, 300)
        }

        iv_cover.setOnClickListener {
            if (mData.size > mCurrentIndex) {
                val bean = mData[mCurrentIndex]
                bean?.let {
                    WebActivity.openWebActivity(context, it.title ?: "", it.url ?: "")
                    if (mCurrentIndex < mData.size - 1) {
                        showNextImage()
                    } else {
                        dismiss()
                    }
                }
            }
        }

        mCurrentIndex = 0
        showImage(mCurrentIndex)

        val window = window
        val layoutParams = window!!.attributes
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.width = (ToolUtil.getScreenWidth(context) * 0.7f).toInt()
        window.attributes = layoutParams
        setCanceledOnTouchOutside(false)
    }

    private fun showNextImage() {
        mCurrentIndex += 1
        showImage(mCurrentIndex)
    }

    private fun showImage(index: Int) {
        if (mData.size > index) {
            val bean = mData[index]
            bean?.let {
                iv_cover?.load(it.thumb ?: "") {
                    crossfade(true)
                }
            }
        }
        if (mData.size <= index + 1) {
            tv_next?.visibility = View.INVISIBLE
        } else {
            tv_next?.visibility = View.VISIBLE
        }
    }


    fun setBanner(data: List<BannerBean>?): BannerDialog {
        mData.clear()
        if (data != null && data.isNotEmpty()) {
            mData.addAll(data)
        }
        mCurrentIndex = 0
        showImage(mCurrentIndex)
        return this
    }


}