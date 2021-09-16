package cn.dianyinhuoban.hm.mvp.me.view.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.Purchase
import com.wareroom.lib_base.ui.adapter.BaseAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import kotlinx.android.synthetic.main.item_member_barchart.view.*
import java.math.BigDecimal
import java.util.*
import kotlin.math.max

class MemberBarchartAdapter(context: Context) :
    BaseAdapter<Purchase, MemberBarchartAdapter.BarCartViewHolder>() {
    private var mContext: Context? = null
    private var mBarMaxHeight: Int = 0
    private var mMaxValue: Long = 0


    init {
        mContext = context
        mBarMaxHeight = DimensionUtils.dp2px(mContext, 110)
    }

    override fun setData(data: List<Purchase>?) {
        if (mData == null) {
            mData = ArrayList()
        } else {
            mData.clear()
        }
        mMaxValue = 0
        if (data != null && data.isNotEmpty()) {
            for (i in data.indices) {
                mMaxValue = max(mMaxValue, data[i]?.num?.toLong() ?: 0)
                mData.add(data[i])
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarCartViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member_barchart, parent, false)
        return BarCartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BarCartViewHolder, position: Int) {
        val itemValue = if (mData == null || position >= mData.size) {
            null
        } else {
            mData[position]
        }
        //柱的高度
        val barHeight = if (mMaxValue <= 0 || TextUtils.isEmpty(itemValue?.num)) {
            BigDecimal.ZERO
        } else {
            itemValue?.num?.toBigDecimal()?.let {
                it.divide(mMaxValue.toDouble().toBigDecimal(), 10, BigDecimal.ROUND_HALF_UP)
                    .multiply(mBarMaxHeight.toBigDecimal())
            }
        }
        //柱的颜色
        val barBackground = if (itemValue?.num?.toLong() == mMaxValue) {
            mContext?.let { ContextCompat.getColor(it, R.color.color_037dff) }
        } else {
            mContext?.let { ContextCompat.getColor(it, R.color.color_D3E8FF) }
        }

        barBackground?.let { holder.itemView.view_bar.setBackgroundColor(it) }
        val layoutParams = holder.itemView.view_bar.layoutParams
        layoutParams.height = barHeight?.toInt() ?: 0
        holder.itemView.view_bar.layoutParams = layoutParams

        holder.itemView.tv_date.text = itemValue?.inputTime ?: "--"
        holder.itemView.tv_value.text = itemValue?.num ?: "--"
        barBackground?.let { holder.itemView.tv_value.setTextColor(it) }
    }

    class BarCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun getItemCount(): Int {
        return if (mData == null) {
            0
        } else {
            mData.size
        }
    }
}