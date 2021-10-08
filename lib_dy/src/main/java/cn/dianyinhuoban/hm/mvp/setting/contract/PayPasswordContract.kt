package cn.dianyinhuoban.hm.mvp.setting.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface PayPasswordContract {
    interface Model {
        fun submitPayPassword(
            oldPassword: String,
            password: String,
            passwordConfirm: String
        ): Observable<Response<EmptyBean?>>

        fun submitPayPassword(
            password: String,
            code: String
        ): Observable<Response<EmptyBean?>>

        fun sendSMS(
            phone: String,
            imageKey: String,
            imageCode: String
        ): Observable<Response<EmptyBean?>>

        fun fetchImageCode(): Observable<Response<ImageCodeBean?>>
    }

    interface Presenter {
        fun submitPayPassword(
            oldPassword: String,
            password: String,
            passwordConfirm: String
        )

        fun submitPayPassword(
            password: String,
            code: String
        )

        fun fetchImageCode()

        fun onSendSMS(phone: String, imageKey: String, imageCode: String)
    }

    interface View : IView {

        fun startSMSCountdown() {}

        fun showImageCode(imageCode: ImageCodeBean?) {}

        fun onSubmitPayPasswordSuccess()
    }
}