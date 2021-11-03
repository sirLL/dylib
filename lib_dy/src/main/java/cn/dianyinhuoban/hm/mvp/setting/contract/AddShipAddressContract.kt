package cn.dianyinhuoban.hm.mvp.setting.contract

import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface AddShipAddressContract {
    interface Model {
        fun submitShipAddress(
            name: String,
            provinceId: String,
            cityId: String,
            districtId: String,
            address: String,
            phone: String,
            id: String
        ): Observable<Response<String?>>

        fun deleteAddress(ids: String): Observable<Response<EmptyBean?>>
    }

    interface Presenter {
        fun submitShipAddress(
            name: String,
            provinceId: String,
            cityId: String,
            districtId: String,
            area: String,
            address: String,
            phone: String,
            id: String
        )

        fun deleteAddress(ids: String)
    }

    interface View : IView {
        fun onSubmitShipAddressSuccess(address: AddressBean)

        fun onDeleteAddressSuccess(ids: String)
    }
}