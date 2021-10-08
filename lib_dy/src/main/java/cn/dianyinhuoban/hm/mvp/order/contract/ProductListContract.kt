package cn.dianyinhuoban.hm.mvp.order.contract

import cn.dianyinhuoban.hm.mvp.bean.PurchaseProductBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface ProductListContract {
    interface Model {
        fun fetchPurchaseProduct(
            typeID: String,
            page: Int
        ): Observable<Response<List<PurchaseProductBean>?>>
    }

    interface Presenter {
        fun fetchPurchaseProduct(typeID: String, page: Int)
    }

    interface View : IView {
        fun bindPurchaseProduct(productData: List<PurchaseProductBean>?)
    }
}