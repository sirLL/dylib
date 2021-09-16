package cn.dianyinhuoban.hm.mvp.auth.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthContract
import cn.dianyinhuoban.hm.mvp.auth.model.AuthModel
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class AuthPresenter(view: AuthContract.View) : BasePresenter<AuthModel, AuthContract.View>(view),
    AuthContract.Presenter {
    override fun buildModel(): AuthModel {
        return AuthModel()
    }

    override fun submitAuth(name: String, idCard: String) {
        mModel?.let {
            if (!isDestroy) {
                view?.showLoading(false)
            }
            addDispose(
                it.submitAuth(name, idCard)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: EmptyBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view.onSubmitAuthSuccess()
                            }
                        }
                    })
            )
        }
    }
}