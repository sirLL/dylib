package cn.dianyinhuoban.hm.mvp.ranking.view

import android.os.Bundle
import android.text.TextUtils

import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MyRank
import cn.dianyinhuoban.hm.mvp.bean.RankBean
import cn.dianyinhuoban.hm.mvp.ranking.contract.RankContract
import cn.dianyinhuoban.hm.mvp.ranking.presenter.RankPresenter
import cn.dianyinhuoban.hm.mvp.ranking.view.adapter.RankingTeamAdapter
import coil.load
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wareroom.lib_base.ui.BaseFragment
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_base.widget.LoadingLayout
import kotlinx.android.synthetic.main.fragment_ranking_team.*
import kotlinx.android.synthetic.main.fragment_ranking_team.loading_layout
import kotlinx.android.synthetic.main.fragment_ranking_team.recycler_view
import kotlinx.android.synthetic.main.fragment_ranking_team.refresh_layout
import kotlinx.android.synthetic.main.fragment_ranking_team.tv_amount_team

class RankingTeamFragment : BaseFragment<RankPresenter?>(), OnRefreshListener, OnLoadMoreListener,
    LoadingLayout.OnViewClickListener, RankContract.View {
    companion object {
        private const val DEF_START_PAGE = 1

        fun newInstance(): RankingTeamFragment {
            val args = Bundle()
            val fragment = RankingTeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mCurrentPage = DEF_START_PAGE
    var mAdapter: RankingTeamAdapter? = null
    private var isLoadMore = false

    override fun getPresenter(): RankPresenter? {
        return RankPresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.fragment_ranking_team
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupRefreshLayout()
        setupLoadLayout()

        loadRankingData()
    }


    private fun setupRecyclerView() {
        mAdapter = RankingTeamAdapter()
        recycler_view.adapter = mAdapter
    }

    private fun setupRefreshLayout() {
        refresh_layout.setRefreshHeader(ClassicsHeader(context))
        refresh_layout.setRefreshFooter(ClassicsFooter(context))
        refresh_layout.setOnRefreshListener(RankingTeamFragment@ this)
        refresh_layout.setOnLoadMoreListener(RankingTeamFragment@ this)
    }

    private fun showEmpty() {
        loading_layout.showEmpty()
        refresh_layout.setEnableLoadMore(false)
    }

    private fun showLoadView() {
        loading_layout.showLoading()
        refresh_layout.setEnableLoadMore(false)
    }

    private fun setupLoadLayout() {
        loading_layout.setOnViewClickListener(RankingTeamFragment@ this)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        isLoadMore = false
        mCurrentPage = DEF_START_PAGE
        request(mCurrentPage)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isLoadMore = true
        request(mCurrentPage)
    }

    override fun onReloadClick() {
        mCurrentPage = DEF_START_PAGE
        request(mCurrentPage)
    }

    private fun request(page: Int) {
        mPresenter?.fetchRank("2", page)
    }

    private fun loadRankingData() {
        showLoadView()
        onRefresh(refresh_layout)
    }

    override fun bindRankData(data: RankBean?) {
        bindMyRank(data?.myRank)
        if (isLoadMore) {
            mAdapter?.appendData(data?.list)
            refresh_layout.finishLoadMore()
        } else {
            mAdapter!!.data = data?.list
            refresh_layout.finishRefresh()
            if (null == data?.list || data?.list!!.isEmpty()) {
                showEmpty()
            } else {
                mCurrentPage += 1
                loading_layout.showSuccess()
            }
        }
        refresh_layout.setEnableLoadMore(true)
    }

    private fun bindMyRank(myRank: MyRank?) {
        if (mCurrentPage != DEF_START_PAGE) return
        tv_no_team.text = if (TextUtils.isEmpty(myRank?.rank) || "0" == myRank?.rank) {
            "未上榜"
        } else {
            myRank?.rank
        }
        tv_name_team.text = if (!TextUtils.isEmpty(MMKVUtil.getNick())) {
            MMKVUtil.getNick()
        } else {
            MMKVUtil.getUserName()
        }
        tv_amount_team.text = NumberUtils.formatMoney(myRank?.total)
        iv_avatar_team.load(MMKVUtil.getAvatar()) {
            crossfade(true)//淡入效果
            allowHardware(false)
            placeholder(R.drawable.img_avatar_def)
            error(R.drawable.img_avatar_def)
        }
    }
}