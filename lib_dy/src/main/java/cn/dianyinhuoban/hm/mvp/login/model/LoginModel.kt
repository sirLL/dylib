package cn.dianyinhuoban.hm.mvp.login.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.login.contract.LoginContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class LoginModel : BaseModel(),
    LoginContract.Model {

    override fun submitLogin(userName: String, password: String): Observable<Response<UserBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitLogin(userName, password)
    }

}