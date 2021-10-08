package cn.dianyinhuoban.hm.mvp.order.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.hm.mvp.order.contract.ProductListContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class ProductListModel : BaseModel(), ProductListContract.Model {
    override fun fetchPurchaseProduct(
        typeID: String,
        page: Int
    ): Observable<Response<List<PurchaseProductBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPurchaseProduct(typeID, page)
    }
}