package cn.dianyinhuoban.hm.mvp.home.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import cn.dianyinhuoban.hm.qiyu.QYHelper
import com.qiyukf.unicorn.api.Unicorn
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_home.*
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
        initX5WebView()
        Unicorn.initSdk()
        setContentView(R.layout.dy_activity_home)
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
            QYHelper.openQYService(HomeActivity@ this, title)
        }
    }

    /**
     * 底部导航栏
     */
    private fun setupNavigationBar() {
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_home_normal, R.drawable.dy_ic_nav_home_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "首页"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_machine_normal, R.drawable.dy_ic_nav_machine_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "机具管理"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_poster_normal, R.drawable.dy_ic_nav_poster_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "素材库"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_leaderboard_normal, R.drawable.dy_ic_nav_leaderboard_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "排行榜"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_me_normal, R.drawable.dy_ic_nav_me_selector,
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
                            MMKV.defaultMMKV().encode("COMPANY_PHONE", systemItemBean.content)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun initX5WebView() {
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                Log.d("QbSdk", "onCoreInitFinished: ")
            }

            override fun onViewInitFinished(p0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("QbSdk", "onViewInitFinished: $p0")
            }

        })
    }

}