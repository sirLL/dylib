package cn.dianyinhuoban.hm.mvp.order.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.OrderBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface OrderDetailContract {
    interface Model {
        fun submitConfirmReceipt(
            orderID: String
        ): Observable<Response<EmptyBean?>>

        fun fetchPurchaseRecord(
            orderID: String
        ): Observable<Response<List<OrderBean>?>>
    }

    interface Presenter {
        fun fetchPurchaseRecord(
            orderID: String
        )

        fun submitConfirmReceipt(
            orderID: String
        )
    }

    interface View : IView {
        fun bindOrder(order: OrderBean?)

        fun onConfirmReceiptSuccess()
    }
}