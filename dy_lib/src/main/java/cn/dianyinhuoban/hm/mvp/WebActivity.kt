package cn.dianyinhuoban.hm.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_web_html.*

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
        setContentView(R.layout.activity_web_html)
        setTitle(mTitle ?: "")
        web_view.loadUrl(mUrl ?: "")
    }


}