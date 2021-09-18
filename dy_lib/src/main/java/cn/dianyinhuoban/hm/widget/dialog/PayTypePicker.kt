package cn.dianyinhuoban.hm.widget.dialog

import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PayTypeBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.me.contract.MeContract
import cn.dianyinhuoban.hm.mvp.me.presenter.MePresenter
import coil.load
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_pay_type_picker.view.*

class PayTypePicker : BaseBottomPicker<PayTypeBean?, MePresenter>(), MeContract.View {
    private var mCheckedPosition = -1

    companion object {
        fun newInstance(): PayTypePicker {
            val args = Bundle()
            val fragment = PayTypePicker()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTitle(): String {
        return "选择支付方式"
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.dy_item_pay_type_picker
    }

    override fun getPresenter(): MePresenter {
        return MePresenter(this)
    }

    override fun onStart() {
        super.onStart()
        request(DEFAULT_BUFFER_SIZE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PayTypeBean?,
    ) {
        if (itemData != null) {
            viewHolder?.itemView?.iv_cover?.load(itemData.iconRes)
            viewHolder?.itemView?.tv_title?.text = itemData.name
            viewHolder?.itemView?.tv_balance?.text = when (itemData.id) {
                1L -> {
                    "当前余额：¥${NumberUtils.formatMoney(itemData.balance)}"
                }
                2L -> {
                    "当前积分：¥${NumberUtils.formatMoney(itemData.balance)}"
                }
                else -> {
                    ""
                }
            }

            viewHolder?.itemView?.tv_balance?.visibility =
                if (1L == itemData.id || 2L == itemData.id) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            viewHolder?.itemView?.iv_hook?.visibility = if (position == mCheckedPosition) {
                View.VISIBLE
            } else {
                View.GONE
            }
        } else {
            viewHolder?.itemView?.iv_cover?.load(0)
            viewHolder?.itemView?.tv_title?.text = ""
            viewHolder?.itemView?.tv_balance?.text = ""
            viewHolder?.itemView?.iv_hook?.visibility = View.GONE
        }
    }

    override fun onItemClick(d: PayTypeBean?, position: Int) {
        d?.let {
            mCheckedPosition = position
            getAdapter()?.notifyDataSetChanged()
            super.onItemClick(d, position)
        }
    }

    override fun request(page: Int) {
        mPresenter?.fetchPersonalData()
    }

    override fun bindPersonalData(personalBean: PersonalBean?) {
        val payTypeData = mutableListOf<PayTypeBean?>()
        payTypeData.add(
            PayTypeBean(
                1,
                R.drawable.dy_ic_pay_type_balance,
                "余额支付",
                personalBean?.total ?: "0"
            )
        )

        payTypeData.add(
            PayTypeBean(
                2,
                R.drawable.dy_ic_pay_type_integral,
                "积分支付",
                personalBean?.point ?: "0"
            )
        )

//        payTypeData.add(
//            PayTypeBean(
//                3,
//                R.drawable.ic_pay_type_alipay,
//                "支付宝支付",
//                "0"
//            )
//        )
        loadData(payTypeData)
    }


}