package cn.dianyinhuoban.hm

import android.os.Bundle
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter.SimpleViewHolder
import java.util.*

class TestListFragment : BaseListFragment<Any?, IPresenter?>() {
    override fun onRequest(page: Int) {
        val list: MutableList<Any?> = ArrayList()
        for (i in 0..39) {
            list.add(null)
        }
        loadData(list)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_test_list
    }


    override fun getPresenter(): IPresenter? {
        return null
    }

    companion object {
        fun newInstance(): TestListFragment {
            val args = Bundle()
            val fragment = TestListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun convert(viewHolder: SimpleViewHolder?, position: Int, itemData: Any?) {

    }

    override fun onItemClick(data: Any?, position: Int) {

    }
}