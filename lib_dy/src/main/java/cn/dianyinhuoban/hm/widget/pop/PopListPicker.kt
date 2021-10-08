package cn.dianyinhuoban.hm.widget.pop

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.ui.adapter.SimpleAdapter

abstract class PopListPicker<T>(context: Context) : PopupWindow() {
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: SimpleAdapter<T>? = null

    init {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dy_pop_list_picker, null, false)
        mRecyclerView = contentView.findViewById(R.id.recycler_view)
        mAdapter = createAdapter()
        mRecyclerView?.adapter = mAdapter
        setContentView(contentView)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isOutsideTouchable = true

        contentView.findViewById<View>(R.id.view_bg).setOnClickListener {
            dismiss()
        }
    }

    private fun createAdapter(): SimpleAdapter<T> {
        return object : SimpleAdapter<T>(R.layout.dy_item_pop_list_picker) {
            override fun convert(
                viewHolder: SimpleAdapter.SimpleViewHolder?,
                position: Int,
                itemData: T
            ) {
                this@PopListPicker.convert(viewHolder, position, itemData)
            }

            override fun onItemClick(data: T, position: Int) {
                this@PopListPicker.onItemChecked(this@PopListPicker, data, position)
            }
        }
    }

    fun setData(data: List<T>?) {
        mAdapter?.data = data
    }

    abstract fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: T
    )

    abstract fun onItemChecked(pop: PopListPicker<T>, data: T, position: Int)

    fun notifyDataSetChanged(){
        mAdapter?.notifyDataSetChanged()
    }
}