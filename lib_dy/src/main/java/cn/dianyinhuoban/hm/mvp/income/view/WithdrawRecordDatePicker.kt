package cn.dianyinhuoban.hm.mvp.income.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import cn.dianyinhuoban.hm.R
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.wareroom.lib_base.utils.DateTimeUtils
import java.text.SimpleDateFormat
import java.util.*

class WithdrawRecordDatePicker : DialogFragment() {

    companion object {
        fun newInstance(startTime: String, endTime: String): WithdrawRecordDatePicker {
            val args = Bundle()
            args.putString("startTime", startTime)
            args.putString("endTime", endTime)
            val fragment = WithdrawRecordDatePicker()
            fragment.arguments = args
            return fragment
        }
    }

    private var tvStart: TextView? = null
    private var tvEnd: TextView? = null
    private var startDatePicker: DatePicker? = null
    private var endDatePicker: DatePicker? = null
    private var startTime: String = ""
    private var endTime: String = ""

    private var onDateSelectedCallback: OnDateSelectedCallback? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startTime = arguments?.getString("startTime", "") ?: ""
        endTime = arguments?.getString("endTime", "") ?: ""
        val contentView =
            inflater.inflate(R.layout.dy_dialog_withdraw_record_date_picker, container, false)
        tvStart = contentView.findViewById(R.id.tv_start)
        tvEnd = contentView.findViewById(R.id.tv_end)

        tvStart?.setOnClickListener {
            showStartDatePicker()
        }
        tvEnd?.setOnClickListener {
            showEndDatePicker()
        }
        contentView.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            onDateSelectedCallback?.let {
                var startTime = tvStart?.text?.toString() ?: ""
                var endTime = tvEnd?.text?.toString() ?: ""
                it.onDateSelected(startTime, endTime)
            }
            dismiss()
        }
        contentView.findViewById<Button>(R.id.btn_clean).setOnClickListener {
            tvStart?.text = ""
            tvEnd?.text = ""
        }
        contentView.findViewById<ImageView>(R.id.iv_cancel).setOnClickListener {
            dismiss()
        }
        tvStart?.text = startTime
        tvEnd?.text = endTime
        return contentView
    }

    fun setOnDateSelectedCallback(onDateSelectedCallback: OnDateSelectedCallback): WithdrawRecordDatePicker {
        this.onDateSelectedCallback = onDateSelectedCallback
        return this
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val layoutParams = window?.attributes
        setWindowLayoutParams(layoutParams, window)
    }

    private fun setWindowLayoutParams(
        layoutParams: WindowManager.LayoutParams?,
        window: Window?,
    ) {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setWindowAnimations(R.style.DYBaseBottomDialogFragment)
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = layoutParams
    }

    private fun showStartDatePicker() {
        if (startDatePicker == null) {
            startDatePicker = createDatePicker()
            startDatePicker?.setOnDatePickedListener { year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month - 1)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                tvStart?.text = DateTimeUtils.formatDate(
                    calendar.timeInMillis,
                    DateTimeUtils.PATTERN_YYYY_MM_DD
                )
            }
        }
        var defDate = DateEntity.today()
        if (startTime.isNotEmpty()) {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.parse(startTime)
            var calendar = Calendar.getInstance()
            calendar.timeInMillis = date.time
            defDate = DateEntity.target(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        val wheelLayout = startDatePicker?.wheelLayout
        wheelLayout?.setRange(
            DateEntity.target(Calendar.getInstance().get(Calendar.YEAR) - 20, 1, 1),
            DateEntity.today(),
            defDate
        )

        startDatePicker?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    private fun showEndDatePicker() {
        if (endDatePicker == null) {
            endDatePicker = createDatePicker()
            endDatePicker?.setOnDatePickedListener { year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month - 1)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                tvEnd?.text = DateTimeUtils.formatDate(
                    calendar.timeInMillis,
                    DateTimeUtils.PATTERN_YYYY_MM_DD
                )
            }
            var defDate = DateEntity.today()
            if (endTime.isNotEmpty()) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val date = formatter.parse(endTime)
                var calendar = Calendar.getInstance()
                calendar.timeInMillis = date.time
                defDate = DateEntity.target(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
            val wheelLayout = endDatePicker?.wheelLayout
            wheelLayout?.setRange(
                DateEntity.target(Calendar.getInstance().get(Calendar.YEAR) - 20, 1, 1),
                DateEntity.today(),
                defDate
            )
        }


        endDatePicker?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    private fun createDatePicker(): DatePicker {
        var picker = DatePicker(requireActivity())
        val wheelLayout = picker?.wheelLayout
        wheelLayout?.setDateMode(DateMode.YEAR_MONTH_DAY)
        wheelLayout?.setDateLabel("年", "月", "日")


        picker?.titleView?.text = "选择日期"
        picker?.titleView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_333333
            )
        )
        picker?.titleView?.textSize = 18f

        picker?.cancelView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_999999
            )
        )
        picker?.cancelView?.textSize = 14f

        picker?.okView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        picker?.okView?.textSize = 14f

        picker?.contentView?.setBackgroundColor(Color.TRANSPARENT)
        picker?.bodyView?.setBackgroundColor(Color.WHITE)
        picker?.topLineView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.dy_color_divider
            )
        )
        picker?.headerView?.setBackgroundResource(R.drawable.dy_shape_ffffff_radius_top_6)
        picker?.window?.setDimAmount(0.2f)
        return picker
    }

    interface OnDateSelectedCallback {
        fun onDateSelected(startTime: String, endTime: String)
    }

}