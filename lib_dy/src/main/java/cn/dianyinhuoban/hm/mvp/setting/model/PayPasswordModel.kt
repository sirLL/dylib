package cn.dianyinhuoban.hm.mvp.setting.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.setting.contract.PayPasswordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PayPasswordModel : BaseModel(), PayPasswordContract.Model {
    override fun submitPayPassword(
        oldPassword: String,
        password: String,
        passwordConfirm: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitPayPassword(oldPassword, password, passwordConfirm)
    }

    override fun submitPayPassword(
        password: String,
        code: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitPayPassword(password, code)
    }

    override fun sendSMS(
        phone: String,
        imageKey: String,
        imageCode: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .sendSMS(phone, imageCode, imageKey, "3")
    }


    override fun fetchImageCode(): Observable<Response<ImageCodeBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchImageCode()
    }
}