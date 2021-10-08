package cn.dianyinhuoban.hm.mvp.income.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TabEntity
import com.dy.tablayout.listener.CustomTabEntity
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_income_personal_detail.*

class IncomeTeamDetailActivity : BaseActivity<IPresenter?>() {

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income_team_detail)
        setTitle("团队收益明细")
        setRightButtonText("收益排行榜") {
            startActivity(Intent(IncomeTeamDetailActivity@ this,
                IncomeTeamRankActivity::class.java))
        }
        setupTab()
    }

    private fun setupTab() {
        val tabData = ArrayList<CustomTabEntity>()
        tabData.add(TabEntity("日数据"))
        tabData.add(TabEntity("月数据"))
        val fragmentData = ArrayList<Fragment>()
        fragmentData.add(IncomeTeamDetailFragment.newInstance(IncomeTeamDetailFragment.DATA_TYPE_DAY))
        fragmentData.add(IncomeTeamDetailFragment.newInstance(IncomeTeamDetailFragment.DATA_TYPE_MONTH))
        tab_layout.setTabData(tabData, supportFragmentManager, R.id.fl_container, fragmentData)
    }
}