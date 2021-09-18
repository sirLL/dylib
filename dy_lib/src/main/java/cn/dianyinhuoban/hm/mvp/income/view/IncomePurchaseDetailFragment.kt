package cn.dianyinhuoban.hm.mvp.income.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.DetailBean
import cn.dianyinhuoban.hm.mvp.bean.IncomeDetailBean
import cn.dianyinhuoban.hm.mvp.income.contract.IncomeDetailContract
import cn.dianyinhuoban.hm.mvp.income.presenter.IncomeDetailPresenter
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_header_income_purchase_detail.*
import kotlinx.android.synthetic.main.dy_item_income_purchase_detail.view.*
import java.util.*

class IncomePurchaseDetailFragment : BaseListFragment<DetailBean, IncomeDetailPresenter?>(),
    IncomeDetailContract.View {
    private var mDataType: String? = DATA_TYPE_DAY
    private var mDatePicker: DatePicker? = null
    private var mCheckedDate: Date? = null

    companion object {
        const val DATA_TYPE_DAY = "day"
        const val DATA_TYPE_MONTH = "month"

        fun newInstance(dataType: String): IncomePurchaseDetailFragment {
            val args = Bundle()
            args.putString("dataType", dataType)
            val fragment = IncomePurchaseDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_income_purchase_detail
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_income_purchase_detail
    }

    override fun getPresenter(): IncomeDetailPresenter? {
        return IncomeDetailPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataType = arguments?.getString("dataType", DATA_TYPE_DAY)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_date_description.text = if (mDataType == DATA_TYPE_MONTH) {
            "本月采购奖励/元"
        } else {
            "今日采购奖励/元"
        }
        textView1.text = if (mDataType == DATA_TYPE_MONTH) {
            "上月采购奖励/元"
        } else {
            "昨日采购奖励/元"
        }
        tv_date.setOnClickListener {
            showDatePicker()
        }

        onDateChecked(Date())
    }

    override fun onRequest(page: Int) {
        fetchIncomeData(page)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: DetailBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = "采购奖励"
        viewHolder?.itemView?.tv_amount?.text = if (itemData == null) {
            "--"
        } else {
            NumberUtils.numberScale(itemData.price)
        }
        viewHolder?.itemView?.tv_status?.text = if (itemData == null) {
            "--"
        } else {
            NumberUtils.numberScale(itemData.order_no)
        }
        viewHolder?.itemView?.tv_date?.text = if (itemData == null) {
            "--"
        } else {
            DateTimeUtils.getYYYYMMDDHHMMSS((itemData.add_time?.toLong() ?: 0) * 1000)
        }
    }

    override fun onItemClick(data: DetailBean?, position: Int) {

    }

    override fun bindIncomeDetail(incomeDetail: IncomeDetailBean?) {
        bindHeaderData(incomeDetail)
        loadData(incomeDetail?.detailList)
    }

    private fun bindHeaderData(incomeDetail: IncomeDetailBean?) {
        tv_amount.text = if (incomeDetail != null) {
            NumberUtils.numberScale(incomeDetail.current)
        } else {
            "--"
        }
        tv_amount_activation.text = if (incomeDetail != null) {
            NumberUtils.numberScale(incomeDetail.subLeft)
        } else {
            "--"
        }
        tv_amount_transfer.text = if (incomeDetail != null) {
            NumberUtils.numberScale(incomeDetail.subRight)
        } else {
            "--"
        }
    }

    //日期选择
    private fun showDatePicker() {
        if (mDatePicker == null) {
            var nowCalendar = Calendar.getInstance()
            var startCalendar = Calendar.getInstance()
            startCalendar.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR) - 20)
            startCalendar.set(Calendar.MONTH, 0)
            startCalendar.set(Calendar.DAY_OF_MONTH, 1)
            mDatePicker = DatePicker(requireActivity())
            val wheelLayout = mDatePicker?.wheelLayout
            wheelLayout?.setDateMode(
                if (mDataType == DATA_TYPE_DAY) {
                    DateMode.YEAR_MONTH_DAY
                } else {
                    DateMode.YEAR_MONTH
                }
            )
            wheelLayout?.setDateLabel("年", "月", "日")
            wheelLayout?.setRange(
                DateEntity.target(startCalendar.get(Calendar.YEAR), 1, 1),
                DateEntity.today(),
                DateEntity.today()
            )

            mDatePicker?.titleView?.text = "选择日期"
            mDatePicker?.titleView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_333333
                )
            )
            mDatePicker?.titleView?.textSize = 18f

            mDatePicker?.cancelView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_999999
                )
            )
            mDatePicker?.cancelView?.textSize = 14f

            mDatePicker?.okView?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_037dff
                )
            )
            mDatePicker?.okView?.textSize = 14f

            mDatePicker?.contentView?.setBackgroundColor(Color.TRANSPARENT)
            mDatePicker?.bodyView?.setBackgroundColor(Color.WHITE)
            mDatePicker?.topLineView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dy_color_divider
                )
            )
            mDatePicker?.headerView?.setBackgroundResource(R.drawable.dy_shape_ffffff_radius_top_6)
            mDatePicker?.setOnDatePickedListener { year, month, day ->
                val calendar = Calendar.getInstance()
                if (mDataType == IncomeActivationDetailFragment.DATA_TYPE_MONTH) {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month - 1)
                } else {
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                }
                onDateChecked(calendar.time)
            }


//            mDatePicker = TimePickerBuilder(
//                requireContext()
//            ) { date, _ ->
//                if (date != null) {
//                    onDateChecked(date)
//                }
//            }.setType(
//                booleanArrayOf(
//                    true,
//                    true,
//                    mDataType == DATA_TYPE_DAY,
//                    false,
//                    false,
//                    false
//                )
//            )
//                .setCancelColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.color_999999
//                    )
//                )
//                .setSubmitColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.color_037dff
//                    )
//                )
//                .setTitleColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.color_333333
//                    )
//                )
//                .setTitleBgColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.color_white
//                    )
//                )
//                .setSubCalSize(14)
//                .setCancelText("取消")
//                .setSubmitText("确定")
//                .setTitleText("选择时间")
//                .setContentTextSize(16)
//                .setLabel("年", "月", "日", "", "", "")
//                .setRangDate(startCalendar, nowCalendar)
//                .build()
        }
        if (!mDatePicker?.isShowing!!) {
            mDatePicker?.show()
        }
    }

    private fun onDateChecked(date: Date) {
        mCheckedDate = date
        if (mDataType == DATA_TYPE_MONTH) {
            tv_date.text = DateTimeUtils.formatDate(date.time, "yyyy年MM月")
        } else {
            tv_date.text = DateTimeUtils.formatDate(date.time, "yyyy年MM月dd日")
        }
        mRefreshLayout.autoRefresh()
    }

    private fun fetchIncomeData(page: Int) {
        if (mCheckedDate == null) {
            showToast("请选择日期")
            return
        }
        val date = if (mDataType == DATA_TYPE_MONTH) {
            DateTimeUtils.formatDate(mCheckedDate?.time!!, "yyyy-MM")
        } else {
            DateTimeUtils.formatDate(mCheckedDate?.time!!, "yyyy-MM-dd")
        }
        mPresenter?.fetchIncomeDetail("4", date, page)
    }
}