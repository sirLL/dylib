package cn.dianyinhuoban.hm.mvp.auth.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthContract
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class AuthModel : BaseModel(), AuthContract.Model {
    override fun submitAuth(name: String, idCard: String): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitAuth(name, idCard)
    }
}