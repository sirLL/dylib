package cn.dianyinhuoban.hm.mvp.order

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.OrderBean
import cn.dianyinhuoban.hm.mvp.order.contract.OrderListContract
import cn.dianyinhuoban.hm.mvp.order.presenter.OrderListPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_pos_order.view.*

class OrderListFragment : BaseListFragment<OrderBean, OrderListPresenter?>(),
    OrderListContract.View {
    private var mStatus: String? = null
    private val dp2px3: Float by lazy {
        DimensionUtils.dp2px(requireContext(), 3).toFloat()
    }

    companion object {
        fun newInstance(status: String): OrderListFragment {
            val args = Bundle()
            args.putString("status", status)
            val fragment = OrderListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStatus = arguments?.getString("status")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun getPresenter(): OrderListPresenter? {
        return OrderListPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchPurchaseRecordList(mStatus ?: "", page)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_pos_order
    }

    override fun bindPurchaseRecord(data: List<OrderBean>?) {
        loadData(data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: OrderBean?,
    ) {
        viewHolder?.itemView?.tv_order_no?.text = "No: ${itemData?.purchaseNo ?: "--"}"
        viewHolder?.itemView?.tv_order_status?.text = getOrderStatusName(itemData?.status ?: "")
        viewHolder?.itemView?.tv_order_status?.setTextColor(getStatusColor(itemData?.status ?: ""))
        viewHolder?.itemView?.tv_order_product?.text = itemData?.machineName ?: "--"
        viewHolder?.itemView?.tv_order_price?.text = "¥${NumberUtils.formatMoney(itemData?.price)}"
        viewHolder?.itemView?.tv_order_count?.text = "x${itemData?.num}"

        val num = NumberUtils.string2BigDecimal(itemData?.num)
        val price = NumberUtils.string2BigDecimal(itemData?.price)
        val amount = num.multiply(price)
        viewHolder?.itemView?.tv_order_amount?.text = "订单总额：${NumberUtils.formatMoney(amount)}"


        viewHolder?.itemView?.img_order_thumb?.load(itemData?.img ?: "") {
            crossfade(true)
            placeholder(R.drawable.dy_ic_app_logo)
            error(R.drawable.dy_ic_app_logo)
        }
    }

    override fun onItemClick(data: OrderBean?, position: Int) {
        data.let {
            OrderDetailActivity.open(requireContext(), data?.id)
        }

    }

    private fun getOrderStatusName(status: String): String {
        return when (status) {
            "1" -> {
                "未支付"
            }
            "2" -> {
                "未发货"
            }
            "3" -> {
                "已填写快递单号"
            }
            "4" -> {
                "已完成"
            }
            "-1" -> {
                "已退款"
            }
            else -> {
                "--"
            }
        }
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            "1" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            "2" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            "3" -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
            "4" -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
            "-1" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            else -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
        }
    }

}