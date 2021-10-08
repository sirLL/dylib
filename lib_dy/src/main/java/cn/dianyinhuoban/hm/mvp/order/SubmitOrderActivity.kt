package cn.dianyinhuoban.hm.mvp.order

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.event.PaySuccessEvent
import cn.dianyinhuoban.hm.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.hm.mvp.order.contract.ProductListContract
import cn.dianyinhuoban.hm.mvp.order.presenter.ProductListPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.gyf.immersionbar.ImmersionBar
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.widget.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.dy_activity_submit_order.*
import kotlinx.android.synthetic.main.dy_item_submit_order.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class SubmitOrderActivity : BaseActivity<ProductListPresenter?>(), ProductListContract.View {
    var mAdapter: SimpleAdapter<PurchaseProductBean?>? = null
    var mCheckedProduct: PurchaseProductBean? = null
    override fun getRootView(): Int {
        return R.layout.dy_activity_submit_order_root
    }

    override fun isDarkModeEnable(): Boolean {
        return false
    }

    override fun getStatusBarColor(): Int {
        return R.color.dy_base_color_transparent
    }

    override fun getToolbarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun getBackButtonIcon(): Int {
        return R.drawable.dy_base_ic_back_white
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.dy_activity_submit_order)
        setupStatusBar()
        setupRecyclerView()
        setupAction()
        fetchProduct()
    }

    private fun setupAction() {
        tv_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (mCheckedProduct != null) {
                    val num = NumberUtils.string2BigDecimal(tv_count.text.toString())
                    val price = NumberUtils.string2BigDecimal(mCheckedProduct?.price)
                    val amount = num.multiply(price)
                    tv_amount.text = "¥${
                        amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                    } (共计 ${mCheckedProduct?.set_meal ?: "--"} 台)"
                }
            }
        })

        btn_less.setOnClickListener {
            val num = NumberUtils.string2BigDecimal(tv_count.text.toString())
            if (num.toInt() > 1) {
                tv_count.text = num.subtract(BigDecimal.ONE).toPlainString()
            }
        }

        btn_add.setOnClickListener {
            val num = NumberUtils.string2BigDecimal(tv_count.text.toString())
            tv_count.text = num.add(BigDecimal.ONE).toPlainString()
        }

        btn_submit.setOnClickListener {
            if (mCheckedProduct == null) {
                showToast("请选择商品")
            } else {
                ConfirmOrderActivity.openConfirmOrderActivity(
                    this,
                    mCheckedProduct?.id ?: "",
                    mCheckedProduct?.name ?: "",
                    mCheckedProduct?.img ?: "",
                    mCheckedProduct?.price ?: "",
                    tv_count.text.toString()
                )
            }
        }
    }

    private fun setupStatusBar() {
        ImmersionBar.with(this)
            .autoDarkModeEnable(isDarkModeEnable)
            .autoStatusBarDarkModeEnable(isDarkModeEnable)
            .statusBarColor(statusBarColor)
            .flymeOSStatusBarFontColor(statusBarColor)
            .titleBarMarginTop(mToolbar)
            .init()
    }

    private fun setupRecyclerView() {
        recycler_view.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                DimensionUtils.dp2px(this, 10),
                false
            )
        )
        mAdapter =
            object : SimpleAdapter<PurchaseProductBean?>(R.layout.dy_item_submit_order) {
                override fun convert(
                    viewHolder: SimpleViewHolder?,
                    position: Int,
                    itemData: PurchaseProductBean?,
                ) {
                    viewHolder?.itemView?.tv_title?.text = itemData?.name ?: "--"
                    viewHolder?.itemView?.tv_price?.text = "售价${itemData?.price ?: "--"}元"
                    viewHolder?.itemView?.isSelected =
                        (mCheckedProduct != null && itemData != null && mCheckedProduct!!.id == itemData!!.id)
                }

                override fun onItemClick(data: PurchaseProductBean?, position: Int) {
                    data?.let {
                        onProductChecked(it)
                        notifyDataSetChanged()
                    }
                }
            }
        recycler_view.adapter = mAdapter
    }

    override fun getPresenter(): ProductListPresenter? {
        return ProductListPresenter(this)
    }

    private fun fetchProduct() {
        mPresenter?.fetchPurchaseProduct("", 1)
    }

    override fun bindPurchaseProduct(productData: List<PurchaseProductBean>?) {
        if (productData != null && productData.isNotEmpty()) {
            onProductChecked(productData[0])
        } else {
            onProductChecked(null)
        }
        mAdapter?.data = productData
    }

    private fun onProductChecked(productData: PurchaseProductBean?) {
        mCheckedProduct = productData
        val dp2px6: Float = DimensionUtils.dp2px(this, 6).toFloat()
        img_order_product_img.load(mCheckedProduct?.img ?: "") {
            crossfade(true)
            placeholder(R.drawable.ic_app_logo)
            error(R.drawable.ic_app_logo)
            transformations(RoundedCornersTransformation(dp2px6, dp2px6, dp2px6, dp2px6))
        }
        tv_product_title.text = mCheckedProduct?.describe ?: "--"
        tv_product_price.text = if (TextUtils.isEmpty(mCheckedProduct?.price)) {
            "--"
        } else {
            "${NumberUtils.formatMoney(mCheckedProduct?.price)}/台"
        }
        tv_count.text = "1"
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onPaySuccess(event: PaySuccessEvent) {
        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}