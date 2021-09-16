package cn.dianyinhuoban.hm.mvp.order.contract

import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface AddressManagerContract {
    interface Model {
        fun fetchAddressList(
            page: Int
        ): Observable<Response<List<AddressBean>?>>
    }

    interface Presenter {
        fun fetchAddressList(
            page: Int
        )
    }

    interface View : IView {
        fun bindAddressList(data: List<AddressBean>?)
    }
}