package cn.dianyinhuoban.hm.mvp.me.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.me.contract.MeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MeModel : BaseModel(), MeContract.Model {
    override fun fetchPersonalData(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchPersonalData()
    }

    override fun fetchAuthResult(): Observable<Response<AuthResult?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAuthResult()
    }
}