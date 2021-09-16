package cn.dianyinhuoban.hm.mvp.income.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.IncomeItemBean
import com.wareroom.lib_base.ui.adapter.BaseAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.item_income.view.*

class IncomeAdapter : BaseAdapter<IncomeItemBean, IncomeAdapter.IncomeViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null
    private var mActivationBack: String? = null
    private var mPurchaseBack: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_income, parent, false)
        return IncomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        if (position == 0) {
            holder.itemView.iv_income_type.setImageResource(R.drawable.ic_income_activation)
            holder.itemView.tv_amount.text = if (null == mActivationBack) {
                "--"
            } else {
                NumberUtils.formatMoney(mActivationBack)
            }
            holder.itemView.tv_title.text = "激活返现"
            holder.itemView.tv_other.text = "激活返现明细"
        } else {
            holder.itemView.iv_income_type.setImageResource(R.drawable.ic_income_purchase)
            holder.itemView.tv_amount.text = if (null == mPurchaseBack) {
                "--"
            } else {
                NumberUtils.formatMoney(mPurchaseBack)
            }
            holder.itemView.tv_title.text = "采购奖励"
            holder.itemView.tv_other.text = "采购奖励明细"
        }
        var data: IncomeItemBean? = null
        if (mData != null && mData.size > position) {
            data = mData[position]
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(data, position)
        }
    }

    fun setActivationBack(activationBack: String?) {
        mActivationBack = activationBack
        notifyItemChanged(0)
    }

    fun setPurchaseBack(purchaseBack: String?) {
        mPurchaseBack = purchaseBack
        notifyItemChanged(1)
    }

    override fun getItemCount(): Int {
        return 2
    }

    class IncomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnItemClickListener {
        fun onItemClick(itemBean: IncomeItemBean?, position: Int)
    }
}