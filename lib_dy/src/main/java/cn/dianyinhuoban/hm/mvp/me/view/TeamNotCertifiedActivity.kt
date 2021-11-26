package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TeamNotCertifiedActivity : BaseActivity<IPresenter?>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_team_not_certified)
        setTitle("未实名")
        supportFragmentManager.beginTransaction().add(R.id.fl_container, TeamNotCertifiedFragment.newInstance(), "TeamNotCertifiedFragment").commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}