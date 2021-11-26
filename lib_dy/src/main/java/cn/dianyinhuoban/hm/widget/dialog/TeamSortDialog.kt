package cn.dianyinhuoban.hm.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import cn.dianyinhuoban.hm.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TeamSortDialog : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(): TeamSortDialog {
            val args = Bundle()
            val fragment = TeamSortDialog()
            fragment.arguments = args
            return fragment
        }

        const val SORT_DOWN = "DESC"
        const val SORT_UP = "ASC"
    }

    private var ivCancel: ImageView? = null
    private var tvAmountDown: TextView? = null
    private var tvAmountUp: TextView? = null
    private var tvDateDown: TextView? = null
    private var tvDateUp: TextView? = null

    private var mAmountSort = SORT_DOWN
    private var mDateSort = ""
    private var mOnSortCallBack: OnSortCallBack? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.dy_dialog_team_sort, container, false)
        ivCancel = contentView?.findViewById(R.id.iv_cancel)
        tvAmountDown = contentView?.findViewById(R.id.tv_amount_down)
        tvAmountUp = contentView?.findViewById(R.id.tv_amount_up)
        tvDateDown = contentView?.findViewById(R.id.tv_date_down)
        tvDateUp = contentView?.findViewById(R.id.tv_date_up)
        tvAmountDown?.isSelected = SORT_DOWN == mAmountSort
        tvAmountUp?.isSelected = SORT_UP == mAmountSort
        tvDateDown?.isSelected = SORT_DOWN == mDateSort
        tvDateUp?.isSelected = SORT_UP == mDateSort
        tvDateDown?.setOnClickListener {
            if (!it.isSelected) {
                it.isSelected = true
                tvDateUp?.isSelected = false
            } else {
                it.isSelected = false
            }
        }
        tvDateUp?.setOnClickListener {
            if (!it.isSelected) {
                it.isSelected = true
                tvDateDown?.isSelected = false
            } else {
                it.isSelected = false
            }
        }
        tvAmountDown?.setOnClickListener {
            if (!it.isSelected) {
                it.isSelected = true
                tvAmountUp?.isSelected = false
            } else {
                it.isSelected = false
            }
        }
        tvAmountUp?.setOnClickListener {
            if (!it.isSelected) {
                it.isSelected = true
                tvAmountDown?.isSelected = false
            } else {
                it.isSelected = false
            }
        }
        ivCancel?.setOnClickListener {
            dismiss()
        }
        contentView.findViewById<TextView>(R.id.tv_reset).setOnClickListener {
            tvAmountDown?.isSelected = true
            tvAmountUp?.isSelected = false
            tvDateDown?.isSelected = false
            tvDateUp?.isSelected = false
        }
        contentView.findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            var amountSort = ""
            var dateSort = ""
            tvAmountDown?.let {
                if (it.isSelected) amountSort = SORT_DOWN
            }
            tvAmountUp?.let {
                if (it.isSelected) amountSort = SORT_UP
            }
            tvDateDown?.let {
                if (it.isSelected) dateSort = SORT_DOWN
            }
            tvDateUp?.let {
                if (it.isSelected) dateSort = SORT_UP
            }
            mOnSortCallBack?.onSort(amountSort, dateSort)
            dismiss()
        }
        return contentView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setOnSortCallBack(onSortCallBack: OnSortCallBack) {
        mOnSortCallBack = onSortCallBack
    }

    fun show(manager: FragmentManager, tag: String, amountSort: String, dateSort: String) {
        mAmountSort = amountSort
        mDateSort = dateSort
        show(manager, tag)
    }

    interface OnSortCallBack {
        fun onSort(amountSort: String, dateSort: String);
    }
}