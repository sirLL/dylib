package cn.dianyinhuoban.hm.mvp.order.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.bean.PayInfoBean
import cn.dianyinhuoban.hm.mvp.order.contract.CreateOrderContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class CreateOrderModel : BaseModel(), CreateOrderContract.Model {
    override fun fetchAddress(page: Int): Observable<Response<List<AddressBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAddressList(page)
    }

    override fun submitPurchaseOrder(
        productID: String,
        num: String,
        addressID: String,
        payType: String,
        password: String,
    ): Observable<Response<PayInfoBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitPurchaseOrder(productID, num, addressID, payType, password)
    }
}