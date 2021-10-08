package cn.dianyinhuoban.hm.mvp.poster.view

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PosterTypeBean
import cn.dianyinhuoban.hm.mvp.poster.contract.PosterTypeContract
import cn.dianyinhuoban.hm.mvp.poster.presenter.PosterTypePresenter
import com.wareroom.lib_base.ui.BaseFragment
import kotlinx.android.synthetic.main.dy_fragment_poster.*

class PosterFragment : BaseFragment<PosterTypePresenter>(), PosterTypeContract.View {
    companion object {
        fun newInstance(): PosterFragment {
            val args = Bundle()
            val fragment = PosterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): PosterTypePresenter? {
        return PosterTypePresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_poster
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.fetchPosterType()
    }

    override fun bindPosterType(data: List<PosterTypeBean>?) {
        val titles: ArrayList<String> = arrayListOf<String>()
        val fragmentList = arrayListOf<Fragment>()
        data?.let {
            if (it.isNotEmpty()) {
                for (typeBean in it) {
                    typeBean?.let { type ->
                        if (!TextUtils.isEmpty(type.name)) {
                            titles.add(type.name!!)
                            fragmentList.add(PosterListFragment.newInstance(type.id))
                        }
                    }
                }
            }
        }

        if (titles.size > 0) {
            val titleArr = arrayOfNulls<String>(titles.size)
            for (i in 0 until titles.size) {
                titleArr[i] = titles[i]
            }
            tab_layout.setViewPager(
                view_pager,
                titleArr, childFragmentManager, fragmentList
            )
        }
    }

}