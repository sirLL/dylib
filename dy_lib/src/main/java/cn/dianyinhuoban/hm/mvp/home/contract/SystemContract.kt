package cn.dianyinhuoban.hm.mvp.home.contract

import cn.dianyinhuoban.hm.mvp.bean.SystemItemBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface SystemContract {
    interface Model {
        fun fetchSystemSetting(): Observable<Response<List<SystemItemBean>?>>
    }

    interface Presenter {
        fun fetchSystemSetting()
    }

    interface View : IView {
        fun bindSystemBean(systemData: List<SystemItemBean>?)
    }
}