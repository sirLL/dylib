package cn.dianyinhuoban.hm.mvp.machine.view

import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.item_should_pay.view.*

class ShouldPayPicker : BaseBottomPicker<Boolean, IPresenter?>() {
    private var mCheckedPosition = -1

    companion object {
        fun newInstance(): ShouldPayPicker {
            val args = Bundle()
            val fragment = ShouldPayPicker()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_should_pay
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getTitle(): String {
        return "选择是否付款"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request(1)
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: Boolean?
    ) {
        viewHolder?.itemView?.tv_title?.text = if (itemData == true) {
            "是"
        } else {
            "否"
        }
        viewHolder?.itemView?.iv_check_box?.isSelected = mCheckedPosition == position
    }

    override fun onItemClick(d: Boolean?, position: Int) {
        mCheckedPosition = position
        getAdapter()?.notifyDataSetChanged()
        super.onItemClick(d, position)
    }

    override fun request(page: Int) {
        val data = mutableListOf<Boolean>()
        data.add(true)
        data.add(false)
        loadData(data)
    }
}