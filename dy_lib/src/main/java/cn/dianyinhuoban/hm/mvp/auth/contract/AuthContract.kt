package cn.dianyinhuoban.hm.mvp.auth.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface AuthContract {
    interface Model {
        fun submitAuth(name: String, idCard: String): Observable<Response<EmptyBean?>>
    }

    interface Presenter {
        fun submitAuth(name: String, idCard: String)
    }

    interface View : IView {
        fun onSubmitAuthSuccess()
    }
}