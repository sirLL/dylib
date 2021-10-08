package cn.dianyinhuoban.hm.mvp.order.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.OrderBean
import cn.dianyinhuoban.hm.mvp.order.contract.OrderDetailContract
import cn.dianyinhuoban.hm.mvp.order.model.OrderDetailModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class OrderDetailPresenter(view: OrderDetailContract.View) :
    BasePresenter<OrderDetailModel, OrderDetailContract.View>(view), OrderDetailContract.Presenter {
    override fun buildModel(): OrderDetailModel {
        return OrderDetailModel()
    }

    override fun fetchPurchaseRecord(orderID: String) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.fetchPurchaseRecord(orderID)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<OrderBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<OrderBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                val order = if (t?.isNotEmpty()) {
                                    t[0]
                                } else {
                                    null
                                }
                                view?.bindOrder(order)
                            }
                        }
                    })
            )
        }
    }

    override fun submitConfirmReceipt(orderID: String) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.submitConfirmReceipt(orderID)
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
                                view?.onConfirmReceiptSuccess()
                                fetchPurchaseRecord(orderID)
                            }

                        }
                    })
            )
        }
    }
}