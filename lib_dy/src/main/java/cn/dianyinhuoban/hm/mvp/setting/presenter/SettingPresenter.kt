package cn.dianyinhuoban.hm.mvp.setting.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.*
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.hm.mvp.setting.model.SettingModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class SettingPresenter(view: SettingContract.View) :
    BasePresenter<SettingModel, SettingContract.View>(view), SettingContract.Presenter {
    override fun buildModel(): SettingModel {
        return SettingModel()
    }

    override fun getJiangWuTangList(page: Int) {
        if (!isDestroy) {
            mModel?.let {
                addDispose(
                    it.getJiangWuTangList(page)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object :
                            CustomResourceSubscriber<MutableList<JiangWuTangBean>>() {
                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    handleError(exception)
                                }
                            }

                            override fun onComplete() {
                                super.onComplete()
                                if (!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onNext(t: MutableList<JiangWuTangBean>) {
                                super.onNext(t)
                                if (!isDestroy) {
                                    view?.onLoadJWTList(t)
                                }
                            }
                        })
                )
            }
        }
    }

    override fun authApply() {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.authApply()
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean>() {
                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    exception?.message?.let { it1 -> view?.onApplyFail(it1) }
                                }
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
                                    view?.onApplySuccess()
                                }
                            }
                        })
                )
            }
        }
    }


    override fun fetchAuthResult() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.fetchAuthResult()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<AuthResult?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: AuthResult) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindAuthResult(t)
                            }
                        }
                    })
            )
        }
    }

    override fun exit() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.exit()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: EmptyBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onExit()
                            }
                        }
                    })
            )
        }
    }
}