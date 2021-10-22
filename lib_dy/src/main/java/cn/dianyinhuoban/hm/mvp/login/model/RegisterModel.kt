package cn.dianyinhuoban.hm.mvp.login.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.login.contract.RegisterContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class RegisterModel : BaseModel(), RegisterContract.Model {
    override fun sendSMS(
        phone: String,
        imageKey: String,
        imageCode: String,
        type: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .sendSMS(phone, imageCode, imageKey, type)
    }


    override fun fetchImageCode(): Observable<Response<ImageCodeBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchImageCode()
    }

    override fun register(
        userName: String,
        msmCode: String,
        password: String,
        payPassword: String,
        inviteCode: String,
        deviceID: String
    ): Observable<Response<UserBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitRegister(userName, msmCode, password, payPassword, inviteCode, deviceID)
    }

    override fun forgetPassword(
        phone: String,
        code: String,
        password: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .forgetPassword(phone, code, password)
    }
}