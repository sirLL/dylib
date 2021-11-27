package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MeContract {
    interface Model {
        fun fetchPersonalData(): Observable<Response<PersonalBean?>>

        fun fetchAuthResult(): Observable<Response<AuthResult?>>
    }

    interface Presenter {
        fun fetchPersonalData()

        fun fetchAuthResult()
    }

    interface View : IView {
        fun bindPersonalData(personalBean: PersonalBean?)

        fun bindAuthResult(authResult: AuthResult)
    }
}