package cn.dianyinhuoban.hm.mvp.login.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface RegisterContract {

    interface Model {

        fun sendSMS(
            phone: String,
            imageKey: String,
            imageCode: String
        ): Observable<Response<EmptyBean?>>

        fun fetchImageCode(): Observable<Response<ImageCodeBean?>>

        fun register(
            userName: String,
            msmCode: String,
            password: String,
            payPassword: String,
            inviteCode: String,
            deviceID: String
        ): Observable<Response<UserBean?>>

        fun forgetPassword(
            phone: String,
            code: String,
            password: String,
        ): Observable<Response<EmptyBean?>>


    }

    interface Presenter {
        fun fetchImageCode()

        fun onSendSMS(phone: String, imageKey: String, imageCode: String)

        fun register(
            userName: String,
            msmCode: String,
            password: String,
            payPassword: String,
            inviteCode: String,
            deviceID: String
        )

        fun forgetPassword(
            phone: String,
            code: String,
            password: String
        )

    }

    interface View : IView {
        fun startSMSCountdown() {}

        fun showImageCode(imageCode: ImageCodeBean?) {}

        fun onRegisterSuccess() {}

        fun onForgetSuccess() {}
    }
}