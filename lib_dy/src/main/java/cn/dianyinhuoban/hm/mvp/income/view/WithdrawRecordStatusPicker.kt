package cn.dianyinhuoban.hm.mvp.income.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.WithdrawRecordStatusBean
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.adapter.SimpleAdapter

class WithdrawRecordStatusPicker : BaseBottomPicker<WithdrawRecordStatusBean, IPresenter>() {
    companion object {
        fun newInstance(): WithdrawRecordStatusPicker {
            val args = Bundle()
            val fragment = WithdrawRecordStatusPicker()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTitle(): String {
        return "请选择提现状态"
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.dy_item_dialog_withdraw_type_status
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: WithdrawRecordStatusBean?
    ) {
        viewHolder?.setText(R.id.tv_title, itemData?.title ?: "")
    }

    override fun onStart() {
        super.onStart()
        request(1)
    }

    override fun request(page: Int) {
        val data = mutableListOf<WithdrawRecordStatusBean>()
        data.add(WithdrawRecordStatusBean("", "全部"))
        data.add(WithdrawRecordStatusBean("1", "处理中"))
        data.add(WithdrawRecordStatusBean("2", "成功"))
        data.add(WithdrawRecordStatusBean("2", "失败"))
        loadData(data)
    }
}