package cn.dianyinhuoban.hm.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_web_html.*

class WebHtmlActivity : BaseActivity<IPresenter?>() {
    var mContent: String? = null
    var mTitle: String? = null

    companion object {
        fun openWebHtmlActivity(context: Context, title: String, content: String) {
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_html)
        setTitle(mTitle ?: "")
        web_view.loadDataWithBaseURL(
            null,
            getHtmlData(mContent ?: "") ?: "",
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
        val head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>"
        return "<html>$head<body>$bodyHTML</body></html>"
    }

}