package cn.dianyinhuoban.hm.mvp.me.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TeamNotCertifiedActivity : BaseActivity<IPresenter?>() {

    companion object {
        fun open(context: Context, status: String) {
            val intent = Intent(context, TeamNotCertifiedActivity::class.java)
            val bundle = Bundle()
            bundle.putString("status", status)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_team_not_certified)
        setTitle("未实名")
        var status = intent.extras?.getString("status")
        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            TeamNotCertifiedFragment.newInstance(status ?: "1"),
            "TeamNotCertifiedFragment"
        ).commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}