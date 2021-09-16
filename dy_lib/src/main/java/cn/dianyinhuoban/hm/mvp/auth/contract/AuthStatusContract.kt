package cn.dianyinhuoban.hm.mvp.auth.contract

import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface AuthStatusContract {
    interface Model {
        fun fetchAuthResult(): Observable<Response<AuthResult?>>
    }

    interface Presenter {
        fun fetchAuthResult()
    }

    interface View : IView {
        fun bindAuthResult(authResult: AuthResult?)
    }
}