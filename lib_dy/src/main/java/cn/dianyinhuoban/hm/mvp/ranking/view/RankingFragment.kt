package cn.dianyinhuoban.hm.mvp.ranking.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TabEntity
import com.dy.tablayout.listener.CustomTabEntity
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseFragment
import kotlinx.android.synthetic.main.dy_fragment_ranking.tab_layout

class RankingFragment : BaseFragment<IPresenter?>() {
    companion object {

        fun newInstance(): RankingFragment {
            val args = Bundle()
            val fragment = RankingFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_ranking
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
    }

    private fun setupTabLayout() {
        val tabData: ArrayList<CustomTabEntity> = ArrayList()
        tabData.add(TabEntity("激活排行"))
        tabData.add(TabEntity("交易排行"))
        tabData.add(TabEntity("团队排行"))
        val fragmentData: ArrayList<Fragment> = ArrayList()
        fragmentData.add(RankingPersonalFragment.newInstance())
        fragmentData.add(RankingPersonalFragment.newInstance())
        fragmentData.add(RankingTeamFragment.newInstance())
        tab_layout.setTabData(
            tabData,
            childFragmentManager,
            R.id.fl_container,
            fragmentData
        )
    }


}