package cn.dianyinhuoban.hm.mvp.home.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.SystemItemBean
import cn.dianyinhuoban.hm.mvp.home.contract.SystemContract
import cn.dianyinhuoban.hm.mvp.home.presenter.SystemPresenter
import cn.dianyinhuoban.hm.mvp.machine.view.MachineManagerFragment
import cn.dianyinhuoban.hm.mvp.me.view.MeFragment
import cn.dianyinhuoban.hm.mvp.poster.view.PosterActivity
import cn.dianyinhuoban.hm.mvp.poster.view.PosterFragment
import cn.dianyinhuoban.hm.mvp.ranking.view.RankingFragment
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : BaseActivity<SystemPresenter?>(), SystemContract.View {

    override fun getPresenter(): SystemPresenter? {
        return SystemPresenter(this)
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.fetchSystemSetting()
    }

    private fun initView() {
        setupNavigationBar()
        initFragment()
        iv_custom_service.setOnClickListener {
            val title = "${getString(R.string.app_name)}客服"
        }
    }

    /**
     * 底部导航栏
     */
    private fun setupNavigationBar() {
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_home_normal, R.drawable.ic_nav_home_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "首页"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_machine_normal, R.drawable.ic_nav_machine_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "机具管理"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_poster_normal, R.drawable.dy_ic_nav_poster_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "素材库"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_leaderboard_normal, R.drawable.ic_nav_leaderboard_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "排行榜"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_me_normal, R.drawable.ic_nav_me_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "我的"
        )

        navigation_bar.setOnItemTabClickCallBack { index, _ ->
            if (index == 2) {
                startActivity(Intent(HomeActivity@ this, PosterActivity::class.java))
                true
            } else {
                false
            }
        }
    }

    private fun initFragment() {
        val fragmentList: MutableList<Fragment> = mutableListOf()
        fragmentList.add(HomeFragment.newInstance())
        fragmentList.add(MachineManagerFragment.newInstance())
        fragmentList.add(PosterFragment.newInstance())
        fragmentList.add(RankingFragment.newInstance())
        fragmentList.add(MeFragment.newInstance())
        navigation_bar.setupFragments(supportFragmentManager, R.id.fl_container, fragmentList)
        navigation_bar.checkedTab = 0
    }

    override fun bindSystemBean(systemData: List<SystemItemBean>?) {
        systemData?.let { it ->
            for (systemItemBean in it) {
                systemItemBean?.id?.let {
                    when (it) {
                        "1" -> {
                            //安卓版本号
                        }
                        "2" -> {
                            //IOS版本号
                        }
                        "3" -> {
                            //安卓下载链接
                        }
                        "4" -> {
                            //IOS下载链接
                        }
                        "5" -> {
                            //总部电话

                        }
                    }
                }
            }
        }
    }

}