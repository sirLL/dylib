package cn.dianyinhuoban.hm.mvp.income.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.IncomeItemBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.contract.IncomeContract
import cn.dianyinhuoban.hm.mvp.income.presenter.IncomePresenter
import cn.dianyinhuoban.hm.mvp.income.view.adapter.IncomeAdapter
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.widget.LoadingLayout
import kotlinx.android.synthetic.main.dy_activity_image_status_bar.*
import kotlinx.android.synthetic.main.dy_activity_income.*
import kotlinx.android.synthetic.main.dy_header_income.*


class IncomeActivity : BaseActivity<IncomePresenter?>(), OnRefreshListener, OnLoadMoreListener,
    LoadingLayout.OnViewClickListener, IncomeContract.View {

    companion object {
        private const val TAG = "IncomeActivity"
        private const val DEF_START_PAGE = 1
    }

    private var mCurrentPage = DEF_START_PAGE
    private var isLoadMore = false
    private var mAdapter: IncomeAdapter? = null
    private var mHeaderImageMinHeight = 0
    private var mHeaderImageMaxHeight = 0

    override fun isDarkModeEnable(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return R.color.dy_base_color_transparent
    }

    override fun getToolbarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun getBackButtonIcon(): Int {
        return R.drawable.dy_base_ic_back_white
    }

    override fun getRootView(): Int {
        return R.layout.dy_activity_image_status_bar
    }

    override fun getPresenter(): IncomePresenter? {
        return IncomePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStatusBar()
        setContentView(R.layout.dy_activity_income)

        mHeaderImageMaxHeight =
            (762f / 1125f * DimensionUtils.getScreenWidth(IncomeActivity@ this)).toInt()
        mHeaderImageMinHeight =
            DimensionUtils.getStatusBarHeight(IncomeActivity@ this) + resources.getDimensionPixelOffset(
                R.dimen.dy_base_toolbar_height
            )

        setTitle("收益", ContextCompat.getColor(IncomeActivity@ this, R.color.color_white))
        setupRefreshLayout()
        setupLoadLayout()
        setupRecyclerView()
        setupScrollView()


        tv_withdraw.setOnClickListener {
            startActivity(Intent(IncomeActivity@ this, WithdrawActivity::class.java))
        }

        //个人收益明细
        tv_personal_detail.setOnClickListener {
            startActivity(Intent(IncomeActivity@ this, IncomePersonalDetailActivity::class.java))
        }

        //团队收益明细
        tv_team_detail.setOnClickListener {
            startActivity(Intent(IncomeActivity@ this, IncomeTeamDetailActivity::class.java))
        }

        //提现明细
        tv_withdraw_detail.setOnClickListener {
            startActivity(Intent(IncomeActivity@ this, WithdrawRecordActivity::class.java))
        }
        fetchData()
    }

    override fun initStatusBar() {

    }


    private fun setupStatusBar() {
        ImmersionBar.with(this)
            .autoDarkModeEnable(isDarkModeEnable)
            .autoStatusBarDarkModeEnable(isDarkModeEnable)
            .statusBarColor(statusBarColor)
            .flymeOSStatusBarFontColor(statusBarColor)
            .titleBarMarginTop(mToolbar)
            .init()
    }

    private fun setupRefreshLayout() {
        val refreshHeader = ClassicsHeader(IncomeActivity@ this)
        refreshHeader.setAccentColor(Color.WHITE)
        refreshHeader.setPrimaryColor(Color.TRANSPARENT)
        refresh_layout.setRefreshHeader(refreshHeader)
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setOnRefreshListener(IncomeActivity@ this)
        refresh_layout.setOnLoadMoreListener(IncomeActivity@ this)
    }

    private fun setupLoadLayout() {
        loading_layout.setOnViewClickListener(RankingFragment@ this)
    }

    private fun setupRecyclerView() {
        mAdapter = IncomeAdapter()
        recycler_view.adapter = mAdapter
        mAdapter?.onItemClickListener = object : IncomeAdapter.OnItemClickListener {
            override fun onItemClick(itemBean: IncomeItemBean?, position: Int) {
                if (position == 0) {
                    startActivity(
                        Intent(
                            this@IncomeActivity,
                            IncomeActivationDetailActivity::class.java
                        )
                    )
                } else {
                    startActivity(
                        Intent(
                            this@IncomeActivity,
                            IncomePurchaseDetailActivity::class.java
                        )
                    )
                }
            }
        }
    }

    private fun setupScrollView() {
        scroll_view.setOnScrollChangeListener { _: NestedScrollView, _: Int,
                                                scrollY: Int, _: Int, _: Int ->
            setupHeaderImage(scrollY)
        }
    }

    private fun setupHeaderImage(scrollY: Int) {
        val layoutParams = iv_header.layoutParams
        val height = mHeaderImageMaxHeight - scrollY;
        if (mHeaderImageMinHeight >= height) {
            layoutParams.height = mHeaderImageMinHeight
        } else {
            layoutParams.height = height
        }
        iv_header.layoutParams = layoutParams
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        isLoadMore = false
        fetchData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isLoadMore = true
        fetchData()
    }

    override fun onReloadClick() {
        isLoadMore = false
        mCurrentPage = 1;
        fetchData()
    }

    private fun fetchData() {
        mPresenter?.fetchIncome()
    }


    override fun bindIncomeData(personalBean: PersonalBean?) {
        refresh_layout.finishRefresh()
        mAdapter?.setActivationBack(personalBean?.personalActive)
        mAdapter?.setPurchaseBack(personalBean?.purchase)
        tv_amount.text = if (personalBean == null) {
            "--"
        } else {
            NumberUtils.formatMoney(personalBean.total)
        }
        tv_amount_personal.text = if (personalBean == null) {
            "--"
        } else {
            NumberUtils.formatMoney(personalBean.personal)
        }
        tv_amount_team.text = if (personalBean == null) {
            "--"
        } else {
            NumberUtils.formatMoney(personalBean.team)
        }
    }

}