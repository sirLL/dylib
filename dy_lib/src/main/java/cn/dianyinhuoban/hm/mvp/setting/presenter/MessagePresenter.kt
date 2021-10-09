package cn.dianyinhuoban.hm.mvp.setting.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.NotifyMessageBean
import cn.dianyinhuoban.hm.mvp.setting.contract.MessageContract
import cn.dianyinhuoban.hm.mvp.setting.model.MessageModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider


class MessagePresenter(view: MessageContract.View) : BasePresenter<MessageModel,  MessageContract.View>(view),

    MessageContract.Presenter {

    override fun buildModel(): MessageModel {
        return MessageModel()
    }

    override fun getMessageList(page: Int, type: String, isFresh: String) {
        if (!isDestroy) {
            mModel?.let {
                addDispose(
                    it.getMessageList(page, type, isFresh)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<MutableList<NotifyMessageBean>>() {
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

                            override fun onNext(t: MutableList<NotifyMessageBean>) {
                                super.onNext(t)
                                if (!isDestroy) {
                                    view?.onLoadMessageList(t)
                                }
                            }
                        })
                )
            }
        }
    }

    override fun getMessageDetail(id: String) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.getMessageDetail(id)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<NotifyMessageBean>() {
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

                            override fun onNext(t: NotifyMessageBean) {
                                super.onNext(t)
                                if (!isDestroy) {
                                    view?.onLoadMessageDetail(t)
                                }
                            }
                        })
                )
            }
        }
    }


}