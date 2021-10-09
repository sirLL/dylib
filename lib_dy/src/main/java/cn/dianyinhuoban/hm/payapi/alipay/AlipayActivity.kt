package cn.dianyinhuoban.hm.payapi.alipay

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_web_html.*

class AlipayActivity : BaseActivity<IPresenter?>() {
    var mContent: String? = null
    var mTitle: String? = null

    companion object {
        fun openAlipayActivity(context: Context, title: String, content: String) {
            val intent = Intent(context, AlipayActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("content", content)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mTitle = bundle?.getString("title", "")
        mContent = bundle?.getString("content", "")
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_web_html)
        setTitle(mTitle ?: "")
        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = MyWebViewClient()
        web_view.loadDataWithBaseURL(
            null,
            mContent ?: "",
            "text/html",
            "utf-8",
            null
        )
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

    }
}