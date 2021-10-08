package cn.dianyinhuoban.hm.mvp.income.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TabEntity
import cn.dianyinhuoban.hm.mvp.income.view.IncomePersonalDetailFragment.Companion.DATA_TYPE_DAY
import cn.dianyinhuoban.hm.mvp.income.view.IncomePersonalDetailFragment.Companion.DATA_TYPE_MONTH
import com.dy.tablayout.listener.CustomTabEntity
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_income_personal_detail.*
import kotlin.collections.ArrayList

class IncomePersonalDetailActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_income_personal_detail)
        setTitle("个人收益明细")
        setupTab()
    }

    private fun setupTab() {
        val tabData = ArrayList<CustomTabEntity>()
        tabData.add(TabEntity("日数据"))
        tabData.add(TabEntity("月数据"))
        val fragmentData = ArrayList<Fragment>()
        fragmentData.add(IncomePersonalDetailFragment.newInstance(DATA_TYPE_DAY))
        fragmentData.add(IncomePersonalDetailFragment.newInstance(DATA_TYPE_MONTH))
        tab_layout.setTabData(tabData, supportFragmentManager, R.id.fl_container, fragmentData)
    }

}