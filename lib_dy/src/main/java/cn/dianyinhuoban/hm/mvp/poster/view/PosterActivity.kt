package cn.dianyinhuoban.hm.mvp.poster.view

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PosterTypeBean
import cn.dianyinhuoban.hm.mvp.poster.contract.PosterTypeContract
import cn.dianyinhuoban.hm.mvp.poster.presenter.PosterTypePresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_poster.*


class PosterActivity : BaseActivity<PosterTypePresenter?>(), PosterTypeContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("素材库")
        setContentView(R.layout.activity_poster)
        mPresenter?.fetchPosterType()
    }

    override fun getPresenter(): PosterTypePresenter? {
        return PosterTypePresenter(this)
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
                titleArr, supportFragmentManager, fragmentList
            )
        }
    }
}