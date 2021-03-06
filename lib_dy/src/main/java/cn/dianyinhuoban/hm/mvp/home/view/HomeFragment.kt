package cn.dianyinhuoban.hm.mvp.home.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.CountdownTextUtils
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.bean.CustomModel
import cn.dianyinhuoban.hm.mvp.WebHtmlActivity
import cn.dianyinhuoban.hm.mvp.bean.BannerBean
import cn.dianyinhuoban.hm.mvp.bean.HomeDataBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.home.contract.HomeContract
import cn.dianyinhuoban.hm.mvp.home.presenter.HomePresenter
import cn.dianyinhuoban.hm.mvp.home.view.adapter.ImageBannerAdapter
import cn.dianyinhuoban.hm.mvp.income.view.IncomePersonalDetailActivity
import cn.dianyinhuoban.hm.mvp.income.view.IncomeTeamDetailActivity
import cn.dianyinhuoban.hm.mvp.setting.view.EditInfoActivity
import cn.dianyinhuoban.hm.mvp.setting.view.MessageActivity
import cn.dianyinhuoban.hm.widget.dialog.BannerDialog
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.sunfusheng.marqueeview.MarqueeView
import com.wareroom.lib_base.ui.BaseFragment
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.dy_base_bottom_picker.*
import kotlinx.android.synthetic.main.dy_fragment_home.*
import kotlinx.android.synthetic.main.dy_fragment_home.refresh_layout
import kotlinx.android.synthetic.main.dy_item_home_personal_income.*
import kotlinx.android.synthetic.main.dy_item_home_pk_personal.*
import kotlinx.android.synthetic.main.dy_item_home_pk_team.*
import kotlinx.android.synthetic.main.dy_item_home_team_income.*
import java.math.BigDecimal
import java.util.*

class HomeFragment : BaseFragment<HomePresenter?>(), OnRefreshListener, HomeContract.View {
    private var mHomeDataBean: HomeDataBean? = null
    private var mBannerAdapter: ImageBannerAdapter? = null
    private val mPersonalCountdownUtils: CountdownTextUtils by lazy {
        CountdownTextUtils(this)
    }
    private val mTeamCountdownUtils: CountdownTextUtils by lazy {
        CountdownTextUtils(this)
    }

    companion object {
        const val RC_EDIT_TEAM_NAME = 1024

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): HomePresenter? {
        return HomePresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefreshLayout()
        //??????
        iv_share.setOnClickListener {
            startActivity(Intent(context, ShareQRActivity::class.java))
        }
        //????????????
        iv_message.setOnClickListener {
            startActivity(Intent(context, MessageActivity::class.java))
        }
        tv_tab_day.isSelected = true
        tv_tab_day.setOnClickListener {
            if (tv_tab_day.isSelected) return@setOnClickListener
            tv_tab_day.isSelected = true
            tv_tab_month.isSelected = false
            bindPersonalIncomeData()
        }

        tv_tab_month.setOnClickListener {
            if (tv_tab_month.isSelected) return@setOnClickListener
            tv_tab_day.isSelected = false
            tv_tab_month.isSelected = true
            bindPersonalIncomeData()
        }

        tv_tab_day_team.isSelected = true
        tv_tab_day_team.setOnClickListener {
            if (tv_tab_day_team.isSelected) return@setOnClickListener
            tv_tab_day_team.isSelected = true
            tv_tab_month_team.isSelected = false
            bindTeamIncomeData()
        }

        tv_tab_month_team.setOnClickListener {
            if (tv_tab_month_team.isSelected) return@setOnClickListener
            tv_tab_day_team.isSelected = false
            tv_tab_month_team.isSelected = true
            bindTeamIncomeData()
        }

//        tv_team_name.setOnClickListener {
//            if (MMKVUtil.getIsTeamLeader()) {
//                EditInfoActivity.open(this, RC_EDIT_TEAM_NAME, EditInfoActivity.REQ_TYPE_TEAM)
//            }
//        }

        cl_income_personal_container.setOnClickListener {
            startActivity(Intent(context, IncomePersonalDetailActivity::class.java))
        }

        cl_income_team_container.setOnClickListener {
            startActivity(Intent(context, IncomeTeamDetailActivity::class.java))
        }

        setupBannerView()

        marquee_view.setOnItemClickListener { position, _ ->
            mNoticeData?.let {
                if (it.size > position) {
                    val model = it[position]
                    model?.let { customModel ->
                        WebHtmlActivity.openWebHtmlActivity(
                            requireContext(),
                            customModel.title,
                            customModel.content
                        )
                    }
                }
            }
        }

        mPresenter?.fetchDialogBanner()
    }

    private fun setupRefreshLayout() {
        val refreshHeader = ClassicsHeader(context)
        refresh_layout.setRefreshHeader(refreshHeader)
        refresh_layout.setOnRefreshListener(HomeFragment@ this)
        refresh_layout.setEnableLoadMore(false)
    }

    private fun setupBannerView() {
        mBannerAdapter = ImageBannerAdapter(arrayListOf())
        banner_view.addBannerLifecycleObserver(this)//???????????????????????????
            .setAdapter(mBannerAdapter).indicator = CircleIndicator(requireContext())
    }

    override fun onStart() {
        super.onStart()
        requestData()
        marquee_view.startFlipping()
        banner_view?.start()
    }

    override fun onStop() {
        super.onStop()
        marquee_view.stopFlipping()
        banner_view?.stop()
    }

    override fun onDestroy() {
        banner_view?.destroy()
        super.onDestroy()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        requestData()
    }

    private fun requestData() {
        mPresenter?.fetchPersonalData()
        mPresenter?.fetchHomeData()
        mPresenter?.fetchNoticeList()
        mPresenter?.fetchBanner()
    }

    override fun bindHomeData(homeDataBean: HomeDataBean?) {
        refresh_layout.finishRefresh()

        homeDataBean?.let {
            //??????
            it.userInfo?.let { userInfo ->
                tv_nick.text = if (TextUtils.isEmpty(userInfo.name)) {
                    "Hi ${userInfo.username}"
                } else {
                    "Hi ${userInfo.name}"
                }
            }

            //????????????
            iv_message.setImageResource(
                if (it.newNotice > 0) {
                    R.drawable.dy_ic_home_message_point
                } else {
                    R.drawable.dy_ic_home_message
                }
            )
            mHomeDataBean = homeDataBean
            bindPersonalIncomeData()
            bindTeamIncomeData()
            bindPersonalPKData()
            bindTeamPKData()
        }
    }

    //????????????
    private fun bindPersonalIncomeData() {
        refresh_layout.finishRefresh()
        val dayMonth = if (tv_tab_day.isSelected) {
            mHomeDataBean?.personal?.day
        } else {
            mHomeDataBean?.personal?.month
        }

        tv_amount_title.text = if (tv_tab_day.isSelected) {
            "????????????????????????/???"
        } else {
            "????????????????????????/???"
        }
        tv_amount.text = NumberUtils.numberScale(dayMonth?.total)
        tv_no.text = if (TextUtils.isEmpty(dayMonth?.rank) || "0" == dayMonth?.rank) {
            "?????????"
        } else {
            "No.${dayMonth?.rank}"
        }
        if (TextUtils.isEmpty(dayMonth?.rank) || "0" == dayMonth?.rank) {
            tv_no.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_c50018))
        } else {
            tv_no.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_333333))
        }
        tv_income_title.text = if (tv_tab_day.isSelected) {
            "????????????/???"
        } else {
            "????????????/???"
        }
        tv_activation_title.text = if (tv_tab_day.isSelected) {
            "??????????????????/???"
        } else {
            "??????????????????/???"
        }
        tv_machine_amount.text = NumberUtils.numberScale(dayMonth?.inCome)
        tv_activation_amount.text = NumberUtils.numberScale(dayMonth?.activeTrans)
    }

    //????????????
    private fun bindTeamIncomeData() {
        val weekMonth = if (tv_tab_day_team.isSelected) {
            mHomeDataBean?.team?.day
        } else {
            mHomeDataBean?.team?.month
        }

        val teamName = if (!TextUtils.isEmpty(mHomeDataBean?.userInfo?.teamName)) {
            mHomeDataBean?.userInfo?.teamName
        } else {
            "--?????????"
        }
        tv_team_name.text = teamName
        tv_amount_title_team.text = if (tv_tab_day_team.isSelected) {
            "????????????????????????/???"
        } else {
            "????????????????????????/???"
        }
        tv_activation_title_team.text = if (tv_tab_day_team.isSelected) {
            "????????????/???"
        } else {
            "????????????/???"
        }
        tv_amount_team.text = NumberUtils.numberScale(weekMonth?.total)
        tv_activation_amount_team.text = if (weekMonth?.activeMachine.isNullOrBlank()) {
            "0"
        } else {
            NumberUtils.numberScale(
                weekMonth?.activeMachine?.toDouble() ?: 0.0,
                BigDecimal.ROUND_DOWN,
                0
            )
        }
        tv_machine_amount_team.text = NumberUtils.numberScale(weekMonth?.inCome)
        tv_no_team.text = if (TextUtils.isEmpty(weekMonth?.rank) || "0" == weekMonth?.rank) {
            "?????????"
        } else {
            "No.${weekMonth?.rank}"
        }
        if (TextUtils.isEmpty(weekMonth?.rank) || "0" == weekMonth?.rank) {
            tv_no_team.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_c50018))
        } else {
            tv_no_team.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_f7c164))
        }
    }

    //??????PK
    private fun bindPersonalPKData() {
        cl_pk_personal_container.visibility = if (mHomeDataBean?.personalPK == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
        mPersonalCountdownUtils.clear()
        mPersonalCountdownUtils.startCountdownDDHHMM(
            (mHomeDataBean?.personalPK?.endTime?.toLong() ?: 0) * 1000, tv_day, tv_hour, tv_minute
        )
        tv_left_name.text = if (!TextUtils.isEmpty(MMKVUtil.getNick())) {
            MMKVUtil.getNick()
        } else {
            MMKVUtil.getUserName()
        }
        tv_left_no.text = "????????????:${mHomeDataBean?.personalRank?.rank ?: "--"}???"
        tv_left_activation_amount.text =
            "?????????/???:${mHomeDataBean?.personalRank?.activeTrans ?: "--"}"

        tv_right_name.text = mHomeDataBean?.personalPK?.name
        tv_right_no.text = "????????????:${mHomeDataBean?.personalPK?.rank ?: "--"}???"
        tv_right_activation_amount.text =
            "?????????/???:${mHomeDataBean?.personalPK?.activeTrans ?: "--"}"
    }

    //??????PK
    private fun bindTeamPKData() {
        cl_pk_team_container.visibility = if (mHomeDataBean?.teamPK == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
        mTeamCountdownUtils.clear()
        mTeamCountdownUtils.startCountdownDDHHMM(
            (mHomeDataBean?.teamPK?.endTime?.toLong() ?: 0) * 1000, tv_day, tv_hour, tv_minute
        )

        tv_left_name_team.text = if (!TextUtils.isEmpty(mHomeDataBean?.userInfo?.teamName)) {
            mHomeDataBean?.userInfo?.teamName
        } else if (!TextUtils.isEmpty(MMKVUtil.getNick())) {
            "${MMKVUtil.getNick()}?????????"
        } else {
            "${MMKVUtil.getUserName()}?????????"
        }
        tv_left_no_team.text = "????????????:${mHomeDataBean?.teamRank?.rank ?: "--"}???"
        tv_left_activation_amount_team.text =
            "?????????/???:${mHomeDataBean?.teamRank?.activeTrans ?: "--"}"

        tv_right_name_team.text = mHomeDataBean?.teamPK?.name
        tv_right_no_team.text = "????????????:${mHomeDataBean?.teamPK?.rank ?: "--"}???"
        tv_right_activation_amount_team.text =
            "?????????/???:${mHomeDataBean?.teamPK?.activeTrans ?: "--"}"
    }

    var mNoticeData: List<CustomModel>? = null
    override fun bindNoticeList(data: List<CustomModel>?) {
        refresh_layout.finishRefresh()
        mNoticeData = data
        val marqueeView: MarqueeView<CustomModel> = marquee_view as MarqueeView<CustomModel>
        marqueeView.startWithList(mNoticeData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (RC_EDIT_TEAM_NAME == requestCode) {

            }
        }
    }

    private var mBannerDialog: BannerDialog? = null

    override fun bindDialogBanner(data: List<BannerBean>?) {
        if (mBannerDialog == null) {
            mBannerDialog = BannerDialog(requireContext())
        }
        mBannerDialog?.let {
            it.setBanner(data)
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    override fun bindBanner(data: List<BannerBean>?) {
        mBannerAdapter?.setDatas(data)
    }

    override fun bindPersonalData(personalBean: PersonalBean?) {
        MMKVUtil.saveIsTeamLeader(personalBean?.isTeamLeader == 2)
//        if (personalBean?.isTeamLeader == 2) {
//            //????????????????????????
//            tv_team_name.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                null,
//                null,
//                ContextCompat.getDrawable(requireContext(), R.drawable.dy_ic_home_item_edit),
//                null
//            )
//        } else {
//            tv_team_name.setCompoundDrawablesRelativeWithIntrinsicBounds(
//                null,
//                null,
//                null,
//                null
//            )
//        }
    }

    override fun onDestroyView() {
        mBannerDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        super.onDestroyView()
    }
}