package cn.dianyinhuoban.hm.mvp.me.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TabEntity
import com.dy.tablayout.listener.CustomTabEntity
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_member.*

class MemberActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_member)
        setTitle("我的成员")
        setupTabLayout()

        tv_search_bar.setOnClickListener {
            startActivity(Intent(MemberActivity@ this, MemberSearchActivity::class.java))
        }
    }

    private fun setupTabLayout() {
        val tabData: ArrayList<CustomTabEntity> = ArrayList()
        tabData.add(TabEntity("团队成员"))
        tabData.add(TabEntity("个人收益排行"))
        val fragmentData: ArrayList<Fragment> = ArrayList()
        fragmentData.add(MemberFragment.newInstance())
        fragmentData.add(TeamMemberRankFragment.newInstance())
        tab_layout.setTabData(tabData, supportFragmentManager, R.id.fl_container, fragmentData)
    }


}