package cn.dianyinhuoban.hm.mvp.order.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.hm.mvp.order.contract.ProductListContract
import cn.dianyinhuoban.hm.mvp.order.model.ProductListModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class ProductListPresenter(view: ProductListContract.View) :
    BasePresenter<ProductListModel, ProductListContract.View>(view), ProductListContract.Presenter {
    override fun buildModel(): ProductListModel {
        return ProductListModel()
    }

    override fun fetchPurchaseProduct(typeID: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchPurchaseProduct(typeID, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<PurchaseProductBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<PurchaseProductBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindPurchaseProduct(t)
                            }
                        }
                    })
            )
        }
    }
}