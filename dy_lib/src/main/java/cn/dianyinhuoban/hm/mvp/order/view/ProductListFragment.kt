package cn.dianyinhuoban.hm.mvp.order.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.hm.mvp.order.contract.ProductListContract
import cn.dianyinhuoban.hm.mvp.order.presenter.ProductListPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.item_product_list.view.*

class ProductListFragment : BaseListFragment<PurchaseProductBean, ProductListPresenter>(),
    ProductListContract.View {
    private var mTypeID: String? = null

    companion object {
        fun newInstance(typeID: String): ProductListFragment {
            val args = Bundle()
            args.putString("typeID", typeID)
            val fragment = ProductListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTypeID = arguments?.getString("typeID", "")
    }

    private val mDp2px6 by lazy {
        DimensionUtils.dp2px(requireContext(), 6).toFloat()
    }

    override fun getPresenter(): ProductListPresenter {
        return ProductListPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter.fetchPurchaseProduct(mTypeID ?: "", page)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_product_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PurchaseProductBean?
    ) {
        viewHolder?.itemView?.iv_cover?.load(itemData?.img) {
            crossfade(true)
            transformations(RoundedCornersTransformation(mDp2px6, mDp2px6, mDp2px6, mDp2px6))
        }
        viewHolder?.itemView?.tv_title?.text = itemData?.name ?: "--"
        viewHolder?.itemView?.tv_title_des?.text = itemData?.describe ?: "--"
        viewHolder?.itemView?.tv_price?.text = if (TextUtils.isEmpty(itemData?.price)) {
            "--"
        } else {
            "¥${NumberUtils.formatMoney(itemData?.price)}"
        }
    }

    override fun onItemClick(data: PurchaseProductBean?, position: Int) {
        data?.let {
            ProductDetailActivity.openProductDetailActivity(requireContext(), data)
        }
    }

    override fun bindPurchaseProduct(productData: List<PurchaseProductBean>?) {
        loadData(productData)
    }
}