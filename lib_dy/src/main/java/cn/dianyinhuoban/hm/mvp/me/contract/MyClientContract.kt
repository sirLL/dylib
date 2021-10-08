package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.mvp.bean.MyClientBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MyClientContract {
    interface Model {
        fun fetchMyClientBean(page: Int): Observable<Response<List<MyClientBean>?>>
    }

    interface Presenter {
        fun fetchMyClientBean(page: Int)
    }

    interface View : IView {
        fun bindMyClient(data: List<MyClientBean>?)
    }
}