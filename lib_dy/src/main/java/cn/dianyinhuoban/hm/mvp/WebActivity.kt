package cn.dianyinhuoban.hm.mvp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_web_html.*

class WebActivity : BaseActivity<IPresenter?>() {
    var mUrl: String? = null
    var mTitle: String? = null

    companion object {
        fun openWebActivity(context: Context, title: String, url: String) {
            val intent = Intent(context, WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("url", url)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mTitle = bundle?.getString("title", "")
        mUrl = bundle?.getString("url", "")
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_web_html)
        setTitle(mTitle ?: "")
        web_view.settings.javaScriptEnabled = true
        web_view.settings.domStorageEnabled=true
        web_view.settings.allowFileAccess = true
        web_view.settings.setAppCacheEnabled(true)
//        web_view.settings.javaScriptCanOpenWindowsAutomatically = true
        web_view.settings.useWideViewPort = true
        web_view.settings.loadWithOverviewMode = true
        web_view.settings.databaseEnabled = true
        web_view.loadUrl(mUrl ?: "")
        web_view.webViewClient=object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

        }
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }

}