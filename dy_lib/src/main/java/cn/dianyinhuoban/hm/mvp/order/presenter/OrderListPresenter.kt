package cn.dianyinhuoban.hm.mvp.order.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.OrderBean
import cn.dianyinhuoban.hm.mvp.order.contract.OrderListContract
import cn.dianyinhuoban.hm.mvp.order.model.OrderListModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class OrderListPresenter(view: OrderListContract.View) :
    BasePresenter<OrderListModel, OrderListContract.View>(view), OrderListContract.Presenter {
    override fun buildModel(): OrderListModel {
        return OrderListModel()
    }

    override fun fetchPurchaseRecordList(status: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchPurchaseRecordList(status, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<OrderBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<OrderBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindPurchaseRecord(t)
                            }
                        }
                    })
            )
        }
    }
}