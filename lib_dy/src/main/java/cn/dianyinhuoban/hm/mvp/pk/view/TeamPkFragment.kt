package cn.dianyinhuoban.hm.mvp.pk.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.event.LaunchPkSuccessEvent
import cn.dianyinhuoban.hm.mvp.bean.EqualData
import cn.dianyinhuoban.hm.mvp.bean.PkDataBean
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import cn.dianyinhuoban.hm.mvp.bean.TeamData
import cn.dianyinhuoban.hm.mvp.pk.contract.PKContract
import cn.dianyinhuoban.hm.mvp.pk.presenter.PKPresenter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.wareroom.lib_base.ui.BaseFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_activity_pk_team_fragment.*
import kotlinx.android.synthetic.main.dy_item_pk_member.view.*
import kotlinx.android.synthetic.main.dy_item_home_pk.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class TeamPkFragment : BaseFragment<PKPresenter>(), PKContract.View {
    var mPkMember: PkMember? = null
    var mPkData: PkDataBean? = null
    var mCycleData: String = CYCLE_DATA_Y

    companion object {

        const val CYCLE_DATA_Y = "Y"
        const val CYCLE_DATA_P = "P"

        const val REQ_CODE_SEARCH = 2

        fun newInstance(): TeamPkFragment {
            var args = Bundle()
            var fragment = TeamPkFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): PKPresenter? {
        return PKPresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_activity_pk_team_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun launchPkSuccess(event: LaunchPkSuccessEvent) {
        refresh_layout.autoRefresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupListener()
    }

    override fun onLoadPKData(data: PkDataBean) {
        super.onLoadPKData(data)
        mPkData = data

        setupPkData(mPkData!!)

        refresh_layout.finishRefresh()

    }

    private fun initViews() {

        tv_title.visibility = View.GONE
        ll_countdown_container.visibility = View.GONE

        tv_title.textSize = 12F
        val layoutParams = tv_title.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart =
            layoutParams.marginStart + DimensionUtils.dp2px(requireContext(), 6)
        layoutParams.topMargin = DimensionUtils.dp2px(requireContext(), 8);

        tv_right_title.text = "对方战队"
        tv_right_name.text = "尚未选择"
        tv_right_no.visibility = View.INVISIBLE
        tv_right_activation_amount.visibility = View.INVISIBLE
        tv_right_select.visibility = View.VISIBLE
        tv_right_select.text = "选择PK团队"

        tv_left_title.text = "我的战队"
        tv_left_name.text = ""

        tv_yesterday.isSelected = true
        tv_percent.isSelected = false

        val dataList = Collections.emptyList<TeamData>()
        recycler_view_team_pk.adapter = object : SimpleAdapter<TeamData>(R.layout.dy_item_pk_member) {

            override fun convert(viewHolder: SimpleViewHolder?, position: Int, itemData: TeamData) {
                viewHolder?.itemView?.tv_name?.text = if (TextUtils.isEmpty(itemData?.name)) {
                    itemData?.username
                } else {
                    itemData?.name
                }
                viewHolder?.itemView?.tv_activation?.text =
                    if (mCycleData == PersonalPkFragment.CYCLE_DATA_Y) {
                        NumberUtils.formatMoney(itemData?.yesterdayActive)
                    } else {
                        NumberUtils.formatMoney(itemData?.recent7DayActive)
                    }
                viewHolder?.itemView?.tv_transaction?.text =
                    if (mCycleData == PersonalPkFragment.CYCLE_DATA_Y) {
                        NumberUtils.formatMoney(itemData?.yesterdayPos)
                    } else {
                        NumberUtils.formatMoney(itemData?.recently7daysPos)
                    }
            }

            override fun onItemClick(data: TeamData, position: Int) {

            }

        }
        (recycler_view_team_pk.adapter as SimpleAdapter<TeamData>).setData(dataList)
    }

    private fun setupListener() {
        tv_pk_submit.setOnClickListener {
            if (mPkMember != null && mPkData?.equalData == null) {
                PkSettingActivity.open(
                    requireContext(),
                    mPkMember!!, PkSettingActivity.PK_TYPE_TEAM
                )
            } else {
                showToast("请选择PK团队")
            }
        }

        tv_yesterday.setOnClickListener {
            tv_yesterday.isSelected = true
            tv_percent.isSelected = false
            mCycleData = CYCLE_DATA_Y
            mPkData?.let { it1 -> setupPkData(it1) }
        }

        tv_percent.setOnClickListener {
            tv_yesterday.isSelected = false
            tv_percent.isSelected = true
            mCycleData = CYCLE_DATA_P
            mPkData?.let { it1 -> setupPkData(it1) }
        }

        tv_right_select.setOnClickListener {
            PkListActivity.open(this, REQ_CODE_SEARCH, PkMemberFragment.TYPE_TEAM)
        }


        refresh_layout.setEnableRefresh(true)
        refresh_layout.setRefreshHeader(ClassicsHeader(context))
        refresh_layout.setOnRefreshListener {
            mPresenter.getPKData(0, 2)
        }

        refresh_layout.autoRefresh()
    }

    private fun setupPkData(data: PkDataBean) {
        //mine
        tv_left_name.text = data.myData.teamName

        tv_left_no.text =
            if (TextUtils.isEmpty(data.myData.rank)) "当前没有排名" else "当前排名:" + data.myData.rank + " 名"
        tv_left_activation_amount.text = "本月激活额/元：" + data.myData.active

        tv_mine_active.text =
            if (mCycleData.equals(PersonalPkFragment.CYCLE_DATA_Y)) data.myData.yesterday.active else data.myData.recently7days.active
        tv_mine_transaction.text =
            if (mCycleData.equals(PersonalPkFragment.CYCLE_DATA_Y)) data.myData.yesterday.posTrans else data.myData.recently7days.posTrans
        tv_mine_transaction_total.text =
            if (mCycleData.equals(PersonalPkFragment.CYCLE_DATA_Y)) data.myData.yesterday.posTrans else data.myData.recently7days.posTrans

        if (data.equalData != null) {
            tv_right_no.visibility = View.VISIBLE
            tv_right_activation_amount.visibility = View.VISIBLE
            tv_right_select.visibility = View.GONE
            tv_right_select.setOnClickListener(null)
            el_pk_button.visibility = View.GONE

            val equalData = data.equalData!!

            tv_pk_team_name.text = equalData.teamName
            tv_right_name.text = equalData.teamName

            tv_right_activation_amount.text = "本月激活额/元：" + equalData.active
            tv_right_no.text =
                if (TextUtils.isEmpty(equalData.rank)) "当前没有排名" else "当前排名:" + equalData.rank + " 名"

            tv_pk_active.text =
                if (mCycleData.equals(PersonalPkFragment.CYCLE_DATA_Y)) equalData.yesterday.active else equalData.recently7days.active
            tv_pk_transaction.text =
                if (mCycleData.equals(PersonalPkFragment.CYCLE_DATA_Y)) equalData.yesterday.posTrans else equalData.recently7days.posTrans
            tv_pk_transaction_total.text =
                if (mCycleData.equals(PersonalPkFragment.CYCLE_DATA_Y)) equalData.yesterday.posTrans else equalData.recently7days.posTrans

            when (equalData.status) {
                EqualData.STATUS_COMPLETE -> {

                }

                EqualData.STATUS_LAUNCH -> {
                    ll_countdown_container.visibility = View.GONE
                    tv_title.visibility = View.VISIBLE
                    tv_title.text = "等待对方确认中..."
                }

                EqualData.STATUS_RECEIVE -> {
                    ll_countdown_container.visibility = View.VISIBLE
                    tv_title.visibility = View.GONE

                    val formatPkEndTime = DateTimeUtils.formatPkEndTime(equalData.endTime)

                    tv_day.text = formatPkEndTime[0]
                    tv_hour.text = formatPkEndTime[1]
                    tv_minute.text = formatPkEndTime[2]

                }

                EqualData.STATUS_REFUSE -> {

                }
            }

        } else {
            tv_right_no.visibility = View.INVISIBLE
            tv_right_activation_amount.visibility = View.INVISIBLE
            tv_right_select.visibility = View.VISIBLE
            tv_right_name.text = "尚未选择"

            el_pk_button.visibility = View.VISIBLE

        }
        (recycler_view_team_pk.adapter as SimpleAdapter<*>).data = data.teamList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SEARCH && resultCode == Activity.RESULT_OK) {

            val pkMember = data?.getParcelableExtra<PkMember>("data")
            Log.d("SSS", "选择的是：" + pkMember?.teamName)

            pkMember?.let { it -> setupPkMember(it) }

        }
    }

    private fun setupPkMember(data: PkMember) {
        mPkMember = data

        tv_right_name.text = data.teamName
        tv_right_no.visibility = View.VISIBLE
        tv_right_activation_amount.visibility = View.VISIBLE
        tv_right_select.visibility = View.GONE

    }


}