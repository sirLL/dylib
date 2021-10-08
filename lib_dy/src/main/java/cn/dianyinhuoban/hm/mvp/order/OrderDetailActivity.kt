package cn.dianyinhuoban.hm.mvp.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.OrderBean
import cn.dianyinhuoban.hm.mvp.order.contract.OrderDetailContract
import cn.dianyinhuoban.hm.mvp.order.presenter.OrderDetailPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_activity_order_detail.*
import kotlinx.android.synthetic.main.dy_activity_order_detail.tv_status
import kotlinx.android.synthetic.main.dy_item_pos_order.*

class OrderDetailActivity : BaseActivity<OrderDetailPresenter?>(), OrderDetailContract.View {
    private var mOrderID: String? = null
    private val dp2px3: Float by lazy {
        DimensionUtils.dp2px(this, 3).toFloat()
    }

    companion object {
        fun open(context: Context, orderID: String?) {
            val intent = Intent(context, OrderDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("orderID", orderID)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mOrderID = bundle?.getString("orderID", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_order_detail)
        setTitle("采购详情")
        fl_btn_container.setOnClickListener {
            mPresenter?.submitConfirmReceipt(mOrderID ?: "")
        }
        fetchOrder()
    }

    override fun getPresenter(): OrderDetailPresenter? {
        return OrderDetailPresenter(this)
    }

    private fun fetchOrder() {
        mPresenter?.fetchPurchaseRecord(mOrderID ?: "")
    }

    override fun bindOrder(order: OrderBean?) {
        iv_status.setImageResource(getStatusIcon(order?.status ?: ""))
        tv_status.text = getOrderStatusName(order?.status ?: "")
        tv_status_des.text = getOrderStatusDes(order?.status ?: "")
        tv_name_phone.text = "${order?.contact ?: ""}  ${order?.telephone ?: ""}"
        tv_order_area.text = order?.area ?: ""
        tv_order_detail_address.text = order?.address ?: ""
        tv_order_time.text =
            DateTimeUtils.getYYYYMMDDHHMMSS((order?.inputTime?.toLong() ?: 0) * 1000)

        ll_logistics_container.visibility = if ("3" == order?.status || "4" == order?.status) {
            View.VISIBLE
        } else {
            View.GONE
        }
        tv_logistics_company.text = order?.expressBrand ?: ""
        tv_logistics_no.text = order?.expressNo ?: ""

        img_order_thumb?.load(order?.img ?: "") {
            crossfade(true)
            placeholder(R.drawable.dy_ic_app_logo)
            error(R.drawable.dy_ic_app_logo)
            transformations(RoundedCornersTransformation(dp2px3, dp2px3, dp2px3, dp2px3))
        }
        tv_order_no.text = ""
        tv_order_status.text = getOrderStatusName(order?.status ?: "")
        tv_order_product.text = order?.machineName ?: ""
        tv_order_price.text = "¥${NumberUtils.formatMoney(order?.price)}"
        tv_order_count.text = "x${order?.num}"
        val num = NumberUtils.string2BigDecimal(order?.num)
        val price = NumberUtils.string2BigDecimal(order?.price)
        val amount = num.multiply(price)
        tv_order_amount.text == "应付：${NumberUtils.formatMoney(amount)}"

        tv_sn.text = "No: ${order?.purchaseNo ?: "--"}"
        tv_pay_type.text = getPayTypeName(order?.payType ?: "")

        fl_btn_container.visibility = if ("3" == order?.status) {
            View.VISIBLE
        } else {
            View.GONE
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

    private fun getOrderStatusDes(status: String): String {
        return when (status) {
            "1" -> {
                "您尚未付款，请及时付款"
            }
            "2" -> {
                "请耐心等待，上级正在发货中"
            }
            "3" -> {
                "上级已发货，请等待到货"
            }
            "4" -> {
                "恭喜您，采购成功"
            }
            "-1" -> {
                "退款成功"
            }
            else -> {
                ""
            }
        }
    }

    private fun getStatusIcon(status: String): Int {
        return when (status) {
            "1" -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
            "2" -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
            "3" -> {
                R.drawable.dy_ic_order_state_sure
            }
            "4" -> {
                R.drawable.dy_ic_order_state_complete
            }
            "-1" -> {
                R.drawable.dy_ic_order_state_complete
            }
            else -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
        }
    }

    private fun getPayTypeName(payType: String): String {
        return when (payType) {
            "1" -> {
                "余额支付"
            }
            else -> {
                "其他"
            }
        }
    }

    override fun onConfirmReceiptSuccess() {

    }
}