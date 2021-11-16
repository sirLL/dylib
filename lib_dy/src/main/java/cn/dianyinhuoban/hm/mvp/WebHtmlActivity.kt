package cn.dianyinhuoban.hm.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import cn.dianyinhuoban.hm.R
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_web_html.*
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class WebHtmlActivity : BaseActivity<IPresenter?>() {
    var mContent: String? = null
    var mTitle: String? = null

    companion object {
        fun openWebHtmlActivity(context: Context, title: String, content: String) {
            if (content.isNullOrBlank()) return
            val intent = Intent(context, WebHtmlActivity::class.java)
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("content", content)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
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
        web_view.settings.allowFileAccess = true
        web_view.settings.javaScriptCanOpenWindowsAutomatically = true
        web_view.settings.useWideViewPort = true
        web_view.settings.loadWithOverviewMode = true
        web_view.settings.databaseEnabled = true
        web_view.loadDataWithBaseURL(
            null,
            getHtmlData(getURLDecoderString(mContent) ?: "") ?: "",
            "text/html",
            "utf-8",
            null
        )
    }

    /**
     * 加载html标签
     *
     * @param bodyHTML
     * @return
     */
    private fun getHtmlData(bodyHTML: String): String? {
        val data: String = Html.fromHtml(bodyHTML).toString()
        val head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>"
        return "<html>$head<body>$data</body></html>"
    }

    private fun getURLDecoderString(str: String?): String? {
        var result: String? = ""
        if (null == str) {
            return ""
        }
        try {
            result = URLDecoder.decode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return result
    }
}