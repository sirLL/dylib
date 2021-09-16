package cn.dianyinhuoban.hm.mvp.me.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MemberDetailBean
import cn.dianyinhuoban.hm.mvp.machine.view.MachineTransferActivity
import cn.dianyinhuoban.hm.mvp.me.contract.MemberInfoContract
import cn.dianyinhuoban.hm.mvp.me.presenter.MemberInfoPresenter
import cn.dianyinhuoban.hm.mvp.me.view.adapter.MemberBarchartAdapter
import coil.load
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.activity_member_info.*
import java.math.BigDecimal
import java.util.*

class MemberInfoActivity : BaseActivity<MemberInfoPresenter?>(), MemberInfoContract.View,
    OnRefreshListener {
    private var mAdapter: MemberBarchartAdapter? = null
    private var mMemberID: String? = null
    private var mDatePicker: DatePicker? = null
    private var mCheckedDate: Date? = null

    companion object {
        fun openMemberInfoActivity(context: Context, memberID: String) {
            val intent = Intent(context, MemberInfoActivity::class.java)
            val bundle = Bundle()
            bundle.putString("memberID", memberID)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mMemberID = bundle?.getString("memberID", "")
    }

    override fun getPresenter(): MemberInfoPresenter? {
        return MemberInfoPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_info)
        setTitle("成员详情")
        setupRefreshLayout()
        setupRecyclerView()

        tv_transfer.setOnClickListener {
            startActivity(Intent(MemberInfoActivity@ this, MachineTransferActivity::class.java))
        }

        tv_time.setOnClickListener {
            showDatePicker()
        }
        onDateChecked(Date())
    }

    //日期选择
    private fun showDatePicker() {
        if (mDatePicker == null) {
            var nowCalendar = Calendar.getInstance()
            var startCalendar = Calendar.getInstance()
            startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR) - 20)
            startCalendar.set(Calendar.MONTH, 0)
            startCalendar.set(Calendar.DAY_OF_MONTH, 1)

            mDatePicker = DatePicker(this)
            val wheelLayout = mDatePicker?.wheelLayout
            wheelLayout?.setDateMode(DateMode.YEAR_MONTH)
            wheelLayout?.setDateLabel("年", "月", "")
            wheelLayout?.setRange(
                DateEntity.target(startCalendar.get(Calendar.YEAR), 1, 1),
                DateEntity.today(),
                DateEntity.today()
            )

            mDatePicker?.titleView?.text = "选择日期"
            mDatePicker?.titleView?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_333333
                )
            )
            mDatePicker?.titleView?.textSize = 18f

            mDatePicker?.cancelView?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_999999
                )
            )
            mDatePicker?.cancelView?.textSize = 14f

            mDatePicker?.okView?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_037dff
                )
            )
            mDatePicker?.okView?.textSize = 14f

            mDatePicker?.contentView?.setBackgroundColor(Color.TRANSPARENT)
            mDatePicker?.bodyView?.setBackgroundColor(Color.WHITE)
            mDatePicker?.topLineView?.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_divider
                )
            )
            mDatePicker?.headerView?.setBackgroundResource(R.drawable.shape_ffffff_radius_top_6)
            mDatePicker?.setOnDatePickedListener { year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month - 1)
                onDateChecked(calendar.time)
            }
        }
        if (!mDatePicker?.isShowing!!) {
            mDatePicker?.show()
        }
    }

    private fun onDateChecked(date: Date) {
        mCheckedDate = date
        tv_time.text = DateTimeUtils.formatDate(date.time, "yyyy年MM月")
        refresh_layout.autoRefresh()
    }

    private fun setupRefreshLayout() {
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setRefreshHeader(ClassicsHeader(this))
        refresh_layout.setOnRefreshListener(this)
    }

    private fun setupRecyclerView() {
        mAdapter = MemberBarchartAdapter(MemberInfoActivity@ this)
        recycler_view.adapter = mAdapter
    }

    private fun fetchData() {
        mPresenter?.fetchMemberDetail(mMemberID ?: "",
            DateTimeUtils.formatDate(mCheckedDate?.time!!, "yyyy-MM"))
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        fetchData()
    }

    override fun bindMemberDetail(memberDetail: MemberDetailBean?) {
        refresh_layout.finishRefresh()
        mAdapter?.data = memberDetail?.purchaseList

        iv_avatar.load(memberDetail?.avatar ?: "") {
            crossfade(true)
            error(R.drawable.img_avatar_def)
            placeholder(R.drawable.img_avatar_def)
        }
        tv_name.text = if (!TextUtils.isEmpty(memberDetail?.name)) {
            memberDetail?.name
        } else {
            memberDetail?.username ?: ""
        }
        tv_phone.text = memberDetail?.username ?: ""
        tv_member_count.text = memberDetail?.inviteNum ?: ""
        tv_no.text = memberDetail?.teamRank ?: ""

        tv_amount_personal.text = NumberUtils.formatMoney(memberDetail?.weekTrans)
        tv_amount_activation.text = NumberUtils.formatMoney(memberDetail?.monthTrans)

        tv_amount_machine.text = memberDetail?.machineActive ?: "--"
        pb_amount_machine.secondaryProgress = 100
        var progress =
            if (TextUtils.isEmpty(memberDetail?.machineActive) || TextUtils.isEmpty(memberDetail?.machineTotal)) {
                BigDecimal.ZERO
            } else {
                val active = BigDecimal(memberDetail?.machineActive ?: "0")
                val total = BigDecimal(memberDetail?.machineTotal ?: "0")
                if (BigDecimal.ZERO.compareTo(total) == 0 || BigDecimal.ZERO.compareTo(active) == 0) {
                    BigDecimal.ZERO
                } else {
                    active.divide(total, 0, BigDecimal.ROUND_HALF_UP)
                }
            }
        pb_amount_machine.progress = progress.intValueExact()

        tv_amount_purchase.text = memberDetail?.purchaseTotal ?: "--"

        tv_total_transfer.text = NumberUtils.formatMoney(memberDetail?.monthlyTrans)
    }
}