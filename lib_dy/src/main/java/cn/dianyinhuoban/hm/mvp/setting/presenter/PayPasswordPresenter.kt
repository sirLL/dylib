package cn.dianyinhuoban.hm.mvp.setting.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.setting.contract.PayPasswordContract
import cn.dianyinhuoban.hm.mvp.setting.model.PayPasswordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class PayPasswordPresenter(view: PayPasswordContract.View) :
    BasePresenter<PayPasswordModel, PayPasswordContract.View>(view), PayPasswordContract.Presenter {
    override fun buildModel(): PayPasswordModel {
        return PayPasswordModel()
    }

    override fun submitPayPassword(oldPassword: String, password: String, passwordConfirm: String) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.submitPayPassword(oldPassword, password, passwordConfirm)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<EmptyBean>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: EmptyBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onSubmitPayPasswordSuccess()
                            }
                        }
                    })
            )
        }
    }

    override fun submitPayPassword(
        password: String,
        code: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.submitPayPassword(password, code)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<EmptyBean>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: EmptyBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onSubmitPayPasswordSuccess()
                            }
                        }
                    })
            )
        }
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
}