package cn.dianyinhuoban.hm.mvp.login.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.login.contract.RegisterContract
import cn.dianyinhuoban.hm.mvp.login.model.RegisterModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class RegisterPresenter(view: RegisterContract.View) :
    BasePresenter<RegisterModel, RegisterContract.View>(view), RegisterContract.Presenter {
    override fun buildModel(): RegisterModel {
        return RegisterModel()
    }

    //发送短信
    override fun onSendSMS(phone: String, imageKey: String, imageCode: String) {
        mModel?.let {
            it.sendSMS(phone, imageKey, imageCode)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: EmptyBean) {
                        super.onNext(t)
                        //发送短信成功，页面开始倒计时
                        if (!isDestroy) {
                            view?.startSMSCountdown()
                        }
                    }
                })
        }
    }

    override fun fetchImageCode() {
        mModel?.let {
            addDispose(
                it.fetchImageCode()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<ImageCodeBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: ImageCodeBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.showImageCode(t)
                            }
                        }
                    })
            )
        }
    }


    override fun register(
        userName: String,
        msmCode: String,
        password: String,
        payPassword: String,
        inviteCode: String,
        deviceID: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            it.register(userName, msmCode, password, payPassword, inviteCode, deviceID)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<UserBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: UserBean) {
                        super.onNext(t)
                        if (!isDestroy) {
                            view?.hideLoading()
                            view?.onRegisterSuccess()
                        }
                    }
                })
        }
    }

    override fun forgetPassword(phone: String, code: String, password: String) {
        if (!isDestroy) {
            view?.showLoading()
        }

        mModel?.let {
            it.forgetPassword(phone, code, password)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        if (!isDestroy) {
                            view?.hideLoading()
                        }
                    }

                    override fun onNext(t: EmptyBean) {
                        super.onNext(t)
                        if (!isDestroy) {
                            view?.onForgetSuccess()
                        }
                    }
                })
        }
    }

}