package cn.dianyinhuoban.hm.payapi.alipay

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
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
        val map = mapOf(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
            TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true)
        QbSdk.initTbsSettings(map)
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

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // 获取上下文, H5PayDemoActivity为当前页面

            // ------  对alipays:相关的scheme处理 -------
            if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                try {
                    startActivity(Intent("android.intent.action.VIEW", Uri.parse(url)))
                } catch (e: Exception) {
                    AlertDialog.Builder(this@AlipayActivity)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装"
                        ) { _, _ ->
                            val alipayUrl: Uri = Uri.parse("https://d.alipay.com")
                            startActivity(
                                Intent(
                                    "android.intent.action.VIEW",
                                    alipayUrl
                                )
                            )
                        }.setNegativeButton("取消", null).show()
                }
                return true
            }
            // ------- 处理结束 -------
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true
            }
            view.loadUrl(url)
            return true
        }
    }
}