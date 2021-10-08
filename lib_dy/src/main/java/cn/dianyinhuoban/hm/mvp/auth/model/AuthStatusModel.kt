package cn.dianyinhuoban.hm.mvp.auth.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthStatusContract
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class AuthStatusModel : BaseModel(), AuthStatusContract.Model {
    override fun fetchAuthResult(): Observable<Response<AuthResult?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAuthResult()
    }
}