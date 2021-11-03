package cn.dianyinhuoban.hm.mvp.order

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.event.PaySuccessEvent
import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.bean.PayTypeBean
import cn.dianyinhuoban.hm.mvp.me.presenter.MePresenter
import cn.dianyinhuoban.hm.mvp.order.contract.CreateOrderContract
import cn.dianyinhuoban.hm.mvp.order.presenter.CreateOrderPresenter
import cn.dianyinhuoban.hm.mvp.order.view.AddressManagerActivity
import cn.dianyinhuoban.hm.mvp.setting.view.AddShipAddressActivity
import cn.dianyinhuoban.hm.payapi.alipay.AlipayActivity
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import cn.dianyinhuoban.hm.widget.dialog.PayPwdDialog
import cn.dianyinhuoban.hm.widget.dialog.PayTypePicker
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_activity_confirm_order.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

class ConfirmOrderActivity : BaseActivity<CreateOrderPresenter?>(), CreateOrderContract.View {
    var mProductName: String? = null
    var mProductImg: String? = null
    var mProductPrice: String? = null
    var mProductNum: String? = null
    var mProductID: String? = null
    var mPayTypePicker: PayTypePicker? = null

    var mPayType: PayTypeBean? = null
    var mAddress: AddressBean? = null

    companion object {
        const val RC_ADDRESS_PICKER = 1021
        const val RC_ADDRESS_EDIT = 1022
        const val RC_ADDRESS_ADD = 1023

        fun openConfirmOrderActivity(
            context: Context,
            productID: String,
            productName: String,
            productImg: String,
            productPrice: String,
            productNum: String,
        ) {
            val intent = Intent(context, ConfirmOrderActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productID", productID)
            bundle.putString("productName", productName)
            bundle.putString("productImg", productImg)
            bundle.putString("productPrice", productPrice)
            bundle.putString("productNum", productNum)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        bundle?.let {
            mProductName = it.getString("productName", "")
            mProductImg = it.getString("productImg", "")
            mProductPrice = it.getString("productPrice", "")
            mProductNum = it.getString("productNum", "")
            mProductID = it.getString("productID", "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_confirm_order)
        setTitle("确认订单")
        btn_submit.setOnClickListener {
            submitOrder()
        }
        ll_pay_type_container.setOnClickListener {
            showPayTypePicker()
        }
        ll_add_address_container.setOnClickListener {
            AddShipAddressActivity.openAddShipAddressActivity(
                ConfirmOrderActivity@ this, null,
                RC_ADDRESS_ADD
            )
        }
        cl_address_container.setOnClickListener {
            AddressManagerActivity.openAddressManagerActivity(this, RC_ADDRESS_PICKER)
        }

        loadProduct()
    }

    override fun onStart() {
        super.onStart()
        fetchAddress()
    }

    private fun setSubmitEnable() {
        btn_submit.isEnabled =
            mAddress != null && !TextUtils.isEmpty(mProductID)

//        btn_submit.isEnabled =
//            mAddress != null && mPayType != null && !TextUtils.isEmpty(mProductID)
    }

    private fun fetchAddress() {
        mPresenter?.fetchAddress()
    }

    override fun bindDefAddress(addressData: List<AddressBean?>?) {
        var address: AddressBean? = null
        if (!addressData.isNullOrEmpty()) {
            addressData.forEach { addressBean ->
                if (mAddress == null || TextUtils.isEmpty(mAddress?.id)) {
                    if (addressBean != null && !addressBean.id.isNullOrBlank()) {
                        address = addressBean
                        return@forEach
                    }
                } else {
                    if (addressBean != null && !addressBean.id.isNullOrBlank() && addressBean.id == mAddress?.id) {
                        address = addressBean
                        return@forEach
                    }
                }
            }

        }
        if (address == null && mAddress != null) {
            if (!addressData.isNullOrEmpty()) {
                addressData.forEach { addressBean ->
                    if (addressBean != null && !addressBean.id.isNullOrBlank()) {
                        address = addressBean
                        return@forEach
                    }
                }
            }
        }
        bindCheckedAddress(address)
    }

    private fun showPayTypePicker() {
        if (mPayTypePicker == null) {
            mPayTypePicker = PayTypePicker.newInstance()
            mPayTypePicker?.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<PayTypeBean?, MePresenter> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<PayTypeBean?, MePresenter>,
                    t: PayTypeBean?,
                    position: Int,
                ) {
                    t?.let {
                        mPayTypePicker?.dismiss()
                        bindPayType(it)
                    }
                }
            })
        }
        if (!mPayTypePicker!!.isAdded) {
            mPayTypePicker?.show(supportFragmentManager, "PayTypePicker")
        }
    }

    private fun bindPayType(payTypeBean: PayTypeBean?) {
        mPayType = payTypeBean
        tv_pay_type.text = mPayType?.name ?: ""
        setSubmitEnable()
    }

    private fun loadProduct() {
        val dp2px3: Float = DimensionUtils.dp2px(this, 3).toFloat()
        iv_cover.load(mProductImg ?: "") {
            crossfade(true)
            error(R.drawable.dy_ic_app_logo)
            placeholder(R.drawable.dy_ic_app_logo)
            transformations(RoundedCornersTransformation(dp2px3, dp2px3, dp2px3, dp2px3))
        }

        tv_price.text = NumberUtils.formatMoney(mProductPrice)
        tv_name.text = mProductName ?: "--"
        tv_count.text = "x${mProductNum ?: "--"}"
        val num = NumberUtils.string2BigDecimal(mProductNum)
        val price = NumberUtils.string2BigDecimal(mProductPrice)
        tv_amount.text =
            "¥${num.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()}"
        tv_amount_.text =
            "¥${num.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()}"
    }

    override fun getPresenter(): CreateOrderPresenter? {
        return CreateOrderPresenter(this)
    }

    private fun submitOrder() {
        if (TextUtils.isEmpty(mProductID)) {
            showToast("请重新选择商品")
            return
        }
        if (TextUtils.isEmpty(mProductNum)) {
            showToast("请重新选择商品数量")
            return
        }
        if (mPayType == null) {
            showToast("请选择支付方式")
            return
        }
        mPayType?.let {
            if (it.id == 1L || it.id == 2L) {
                showPasswordSubmitOrder()
            } else if (it.id == 5L) {
                submitOrder("")
            }
        }
    }

    private fun showPasswordSubmitOrder() {
        PayPwdDialog(WithdrawActivity@ this)
            .setNumRand(true)
            .setInputComplete(object : PayPwdDialog.OnInputCodeListener {
                override fun inputCodeComplete(dialog: Dialog?, verificationCode: String?) {
                    dialog?.dismiss()
                    submitOrder(verificationCode)
                }

                override fun inputCodeInput(dialog: Dialog?, verificationCode: String?) {
                }
            })
            .show()
    }

    private fun submitOrder(password: String?) {
        if (TextUtils.isEmpty(mProductID)) {
            showToast("请重新选择商品")
            return
        }
        if (TextUtils.isEmpty(mProductNum)) {
            showToast("请重新选择商品数量")
            return
        }
        mPresenter?.submitPurchaseOrder(
            mProductID ?: "",
            mProductNum ?: "",
            mAddress?.id ?: "",
            mPayType?.id?.toString() ?: "",
            password ?: ""
        )
    }

    override fun startAlipay(payInfo: String) {
//        val intent = Intent(this, H5PayActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("url", payInfo)
//        intent.putExtras(bundle)
//        startActivity(intent)
        AlipayActivity.openAlipayActivity(this, "支付宝支付", payInfo)
    }

    override fun startWechatPay() {

    }

    private fun onPaySuccess() {
        EventBus.getDefault().post(PaySuccessEvent())
        showToast("支付成功")
        finish()
    }

    override fun onSubmitOrderSuccess() {
        onPaySuccess()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_ADDRESS_EDIT -> {
                    val address = data?.extras?.getParcelable<AddressBean>("address")
                    bindCheckedAddress(address)
                }

                RC_ADDRESS_PICKER -> {
                    val address = data?.extras?.getParcelable<AddressBean>("address")
                    bindCheckedAddress(address)
                }

                RC_ADDRESS_ADD -> {
                    mPresenter?.fetchAddress()
                }
            }
        }
    }

    private fun bindCheckedAddress(address: AddressBean?) {
        mAddress = address
        if (address == null) {
            cl_address_container.visibility = View.GONE
            ll_add_address_container.visibility = View.VISIBLE
        } else {
            cl_address_container.visibility = View.VISIBLE
            ll_add_address_container.visibility = View.GONE
            tv_name_.text = address.name
            tv_phone_.text = address.phone
            tv_address_.text = "${address.area}${address.address}"
        }
        setSubmitEnable()
    }


}