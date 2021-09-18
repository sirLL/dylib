package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MemberMyRank
import cn.dianyinhuoban.hm.mvp.bean.MemberRankBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberRankContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamMemberRankPresenter
import cn.dianyinhuoban.hm.mvp.me.view.adapter.TeamMemberRankAdapter
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
import kotlinx.android.synthetic.main.dy_fragment_ranking_personal.*


class TeamMemberRankFragment : BaseFragment<TeamMemberRankPresenter?>(), OnRefreshListener,
    OnLoadMoreListener,
    LoadingLayout.OnViewClickListener, TeamMemberRankContract.View {
    companion object {
        private const val DEF_START_PAGE = 1

        fun newInstance(): TeamMemberRankFragment {
            val args = Bundle()
            val fragment = TeamMemberRankFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mCurrentPage = DEF_START_PAGE
    var mAdapter: TeamMemberRankAdapter? = null
    private var isLoadMore = false

    override fun getPresenter(): TeamMemberRankPresenter? {
        return TeamMemberRankPresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_ranking_personal
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupRefreshLayout()
        setupLoadLayout()
        loadRankingData()
    }


    private fun setupRecyclerView() {
        mAdapter = TeamMemberRankAdapter()
        recycler_view.adapter = mAdapter
    }

    private fun setupRefreshLayout() {
        refresh_layout.setRefreshHeader(ClassicsHeader(context))
        refresh_layout.setRefreshFooter(ClassicsFooter(context))
        refresh_layout.setOnRefreshListener(TeamMemberRankFragment@ this)
        refresh_layout.setOnLoadMoreListener(TeamMemberRankFragment@ this)
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
        loading_layout.setOnViewClickListener(TeamMemberRankFragment@ this)
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
        mPresenter?.fetchRank(page)
    }

    private fun loadRankingData() {
        showLoadView()
        onRefresh(refresh_layout)
    }

    override fun bindRank(rankBean: MemberRankBean?) {
        bindMYRank(rankBean?.myRank)
        val data = rankBean?.list
        if (isLoadMore) {
            mAdapter?.appendData(data)
            refresh_layout.finishLoadMore()
        } else {
            mAdapter!!.data = data
            refresh_layout.finishRefresh()
            if (null == data || data.isEmpty()) {
                showEmpty()
            } else {
                mCurrentPage += 1
                loading_layout.showSuccess()
            }
        }
        refresh_layout.setEnableLoadMore(true)
    }

    private fun bindMYRank(myRank: MemberMyRank?) {
        if (mCurrentPage != DEF_START_PAGE) return
        tv_no_my.text = if (TextUtils.isEmpty(myRank?.rank) || "0" == myRank?.rank) {
            "未上榜"
        } else {
            myRank?.rank
        }
        tv_name_my.text = if (!TextUtils.isEmpty(MMKVUtil.getNick())) {
            MMKVUtil.getNick()
        } else {
            MMKVUtil.getUserName()
        }
        tv_amount_my.text = NumberUtils.formatMoney(myRank?.personal)
        iv_avatar_my.load(MMKVUtil.getAvatar()) {
            crossfade(true)//淡入效果
            allowHardware(false)
            placeholder(R.drawable.dy_img_avatar_def)
            error(R.drawable.dy_img_avatar_def)
        }
    }
}