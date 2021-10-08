package cn.dianyinhuoban.hm.mvp.pk.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.*
import cn.dianyinhuoban.hm.mvp.pk.contract.PKContract
import cn.dianyinhuoban.hm.mvp.pk.model.PKModel
import cn.dianyinhuoban.hm.mvp.ranking.contract.RankContract
import cn.dianyinhuoban.hm.mvp.ranking.model.RankModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.Response
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider
import io.reactivex.Observable

class PKPresenter(view: PKContract.View) : BasePresenter<PKModel, PKContract.View>(view),
    PKContract.Presenter {
    override fun buildModel(): PKModel {
        return PKModel()
    }

    override fun getPKData(page: Int, type: Int){
        if (!isDestroy) {
            mModel?.let {
                addDispose(
                    it.getPKData(page, type)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<PkDataBean?>() {
                            override fun onError(exception: ApiException?) {
                                handleError(exception)
                            }

                            override fun onComplete() {
                                super.onComplete()
                                if(!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onNext(t: PkDataBean) {
                                super.onNext(t)
                                if (!isDestroy) {
                                    view?.onLoadPKData(t)
                                }
                            }
                        })
                )
            }
        }
    }

    override fun launchPk(type: String, acceptUid: String, cycle: String, reward: String) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.launchPk(type, acceptUid, cycle, reward)
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
                                    view?.onLaunchPkSuccess()
                                }
                            }
                        })
                )
            }
        }
    }

    override fun getPkMember(type: String, name: String, page: Int) {
        mModel?.let {
            addDispose(
                it.getPkMember(type, name, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MutableList<PkMember>>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onComplete() {
                            super.onComplete()
                            if (!isDestroy) {
                                view?.hideLoading()
                            }
                        }

                        override fun onNext(t: MutableList<PkMember>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.onLoadPkMember(t)
                            }
                        }
                    })
            )
        }
    }

    override fun responsePK(status: String, id: String, peakId: String) {
        if (!isDestroy) {

            view?.showLoading()

            mModel?.let {
                addDispose(
                    it.responsePK(status, id, peakId)
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
                                    view?.onResponsePKSuccess()
                                }
                            }
                        })
                )
            }
        }
    }


}