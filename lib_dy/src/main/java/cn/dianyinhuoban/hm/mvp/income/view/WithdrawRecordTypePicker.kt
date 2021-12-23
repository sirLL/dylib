package cn.dianyinhuoban.hm.mvp.income.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.WithdrawRecordTypeBean
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.adapter.SimpleAdapter

class WithdrawRecordTypePicker : BaseBottomPicker<WithdrawRecordTypeBean, IPresenter>() {
    companion object {
        fun newInstance(): WithdrawRecordTypePicker {
            val args = Bundle()
            val fragment = WithdrawRecordTypePicker()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTitle(): String {
        return "请选择提现类型"
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
        itemData: WithdrawRecordTypeBean?
    ) {
        viewHolder?.setText(R.id.tv_title, itemData?.title ?: "")
    }

    override fun onStart() {
        super.onStart()
        request(1)
    }

    override fun request(page: Int) {
        val data = mutableListOf<WithdrawRecordTypeBean>()
        data.add(WithdrawRecordTypeBean("", "全部"))
        data.add(WithdrawRecordTypeBean("1", "余额"))
        data.add(WithdrawRecordTypeBean("2", "激活返现"))
        loadData(data)
    }
}