package cn.dianyinhuoban.hm.mvp.order.contract

import cn.dianyinhuoban.hm.mvp.bean.OrderBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface OrderListContract {
    interface Model {
        fun fetchPurchaseRecordList(
            status: String,
            page: Int
        ): Observable<Response<List<OrderBean>?>>
    }

    interface Presenter {
        fun fetchPurchaseRecordList(
            status: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindPurchaseRecord(data: List<OrderBean>?)
    }
}