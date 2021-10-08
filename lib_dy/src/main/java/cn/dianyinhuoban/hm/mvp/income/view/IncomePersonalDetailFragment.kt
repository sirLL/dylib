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
import kotlinx.android.synthetic.main.dy_fragment_income_personal_detail.*
import java.math.BigDecimal
import java.util.*
import kotlinx.android.synthetic.main.dy_item_income_personal_detail.view.*

class IncomePersonalDetailFragment : BaseListFragment<DetailBean, IncomeDetailPresenter?>(),
    IncomeDetailContract.View {
    private var mDataType: String? = DATA_TYPE_DAY
    private var mDatePicker: DatePicker? = null
    private var mCheckedDate: Date? = null

    companion object {
        const val DATA_TYPE_DAY = "day"
        const val DATA_TYPE_MONTH = "month"
        fun newInstance(dataType: String): IncomePersonalDetailFragment {
            val args = Bundle()
            args.putString("dataType", dataType)
            val fragment = IncomePersonalDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): IncomeDetailPresenter? {
        return IncomeDetailPresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_income_personal_detail
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
            "本月收益总额/元"
        } else {
            "今日收益总额/元"
        }
        textView1.text = if (mDataType == DATA_TYPE_MONTH) {
            "上月返现/元"
        } else {
            "昨日返现/元"
        }
        tv_date.setOnClickListener {
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
                    R.color.colorPrimary
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
        mPresenter?.fetchIncomeDetail("1", date, page)
    }


    override fun onRequest(page: Int) {
        fetchIncomeData(page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_income_personal_detail
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: DetailBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = NumberUtils.numberScale(itemData?.price)
        viewHolder?.itemView?.tv_time?.text =
            DateTimeUtils.getYYYYMMDDHHMMSS((itemData?.add_time?.toLong() ?: 0) * 1000)
        viewHolder?.itemView?.tv_no?.text = itemData?.order_no ?: "--"
    }

    override fun onItemClick(data: DetailBean?, position: Int) {

    }

    override fun bindIncomeDetail(incomeDetail: IncomeDetailBean?) {
        loadData(incomeDetail?.detailList)
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

//        tv_rate_activation.text = "同比昨日 +24%"
//        tv_rate_activation.setTextColor(
//            ContextCompat.getColor(
//                requireContext(),
//                R.color.color_f60e36
//            )
//        )
//        iv_status_activation.setImageResource(R.drawable.ic_arrow_rise)

        val rate = if (incomeDetail == null) {
            0.0
        } else {
            BigDecimal(incomeDetail.rate).toDouble()
        }
        val rateStr = if (rate >= 0) {
            "+${
                BigDecimal.valueOf(rate).multiply(BigDecimal(100))
                    .setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
            }"
        } else {
            BigDecimal.valueOf(rate).multiply(BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString()
        }

        tv_rate_transfer.text = if (mDataType == DATA_TYPE_MONTH) {
            "同比上月 ${rateStr}%"
        } else {
            "同比昨日 ${rateStr}%"
        }


        tv_rate_transfer.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (rate >= 0) {
                    R.color.color_f60e36
                } else {
                    R.color.color_999999
                }
            )
        )
        iv_status_transfer.setImageResource(
            if (rate >= 0) {
                R.drawable.ic_arrow_rise
            } else {
                R.drawable.ic_arrow_fall
            }
        )
    }

}