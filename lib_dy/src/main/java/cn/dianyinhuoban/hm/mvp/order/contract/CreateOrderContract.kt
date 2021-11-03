package cn.dianyinhuoban.hm.mvp.order.contract

import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PayInfoBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface CreateOrderContract {
    interface Model {
        fun fetchAddress(
            page: Int,
        ): Observable<Response<List<AddressBean>?>>

        fun submitPurchaseOrder(
            productID: String,
            num: String,
            addressID: String,
            payType: String,
            password: String,
        ): Observable<Response<PayInfoBean?>>
    }

    interface Presenter {
        fun fetchAddress()

        fun submitPurchaseOrder(
            productID: String,
            num: String,
            addressID: String,
            payType: String,
            password: String,
        )
    }

    interface View : IView {
        fun startAlipay(payInfo: String)
        fun startWechatPay()
        fun onSubmitOrderSuccess()

        fun bindDefAddress(addressData: List<AddressBean?>?)
    }
}