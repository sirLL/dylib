package cn.dianyinhuoban.hm.mvp.income.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class IncomeTeamRankActivity : BaseActivity<IPresenter?>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_income_team_rank)
        setTitle("成员收益排行榜")
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, IncomeTeamRankFragment.newInstance(), "IncomeTeamRankFragment")
            .commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}