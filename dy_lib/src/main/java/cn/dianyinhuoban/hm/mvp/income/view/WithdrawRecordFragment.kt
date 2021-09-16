package cn.dianyinhuoban.hm.mvp.income.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.WithdrawRecordBean
import cn.dianyinhuoban.hm.mvp.income.contract.WithdrawRecordContract
import cn.dianyinhuoban.hm.mvp.income.presenter.WithdrawRecordPresenter
import cn.dianyinhuoban.hm.util.StringUtil
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.item_withdraw_record.view.*

class WithdrawRecordFragment : BaseListFragment<WithdrawRecordBean, WithdrawRecordPresenter?>(),
    WithdrawRecordContract.View {

    companion object {
        fun newInstance(): WithdrawRecordFragment {
            val args = Bundle()
            val fragment = WithdrawRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.item_withdraw_record
    }

    override fun getPresenter(): WithdrawRecordPresenter? {
        return WithdrawRecordPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchWithdrawRecord(page)
    }

    override fun bindWithdrawRecord(recordData: List<WithdrawRecordBean>?) {
        loadData(recordData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: WithdrawRecordBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = if (itemData == null) {
            "--"
        } else {
            "提现-${itemData.bankName}(${StringUtil.getBankCardEndNo(itemData.bankNo)})"
        }
        viewHolder?.itemView?.tv_amount?.text = if (itemData == null) {
            "--"
        } else {
            NumberUtils.formatMoney(itemData.amount)
        }
        viewHolder?.itemView?.tv_date?.text = if (itemData == null) {
            "--"
        } else {
            DateTimeUtils.getYYYYMMDDHHMMSS(itemData.inputTime.toLong() * 1000)
        }
        viewHolder?.itemView?.tv_status?.text = if (itemData == null) {
            "--"
        } else {
            getStatusName(itemData.status)
        }
    }

    override fun onItemClick(data: WithdrawRecordBean?, position: Int) {

    }

    //状态 1 处理中 2 成功 3 失败
    private fun getStatusName(status: String): String {
        return if (TextUtils.isEmpty(status)) {
            "--"
        } else {
            when (status) {
                "1" -> {
                    "处理中"
                }
                "2" -> {
                    "成功"
                }
                "3" -> {
                    "失败"
                }
                "4" -> {
                    "打款中"
                }
                else -> {
                    "未知状态"
                }
            }
        }
    }


}