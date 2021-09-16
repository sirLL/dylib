package cn.dianyinhuoban.hm.mvp.login.contract

import cn.dianyinhuoban.hm.mvp.bean.UserBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface LoginContract {
    interface Model {
        fun submitLogin(userName: String, password: String): Observable<Response<UserBean?>>
    }

    interface Presenter {
        fun login(userName: String, password: String)
    }

    interface View : IView {
        fun onLoginSuccess()
    }
}