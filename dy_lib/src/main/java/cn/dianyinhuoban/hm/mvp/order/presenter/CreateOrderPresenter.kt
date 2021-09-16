package cn.dianyinhuoban.hm.mvp.order.presenter

import CustomResourceSubscriber
import android.text.TextUtils
import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PayInfoBean
import cn.dianyinhuoban.hm.mvp.order.contract.CreateOrderContract
import cn.dianyinhuoban.hm.mvp.order.model.CreateOrderModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class CreateOrderPresenter(view: CreateOrderContract.View) :
    BasePresenter<CreateOrderModel, CreateOrderContract.View>(view), CreateOrderContract.Presenter {
    override fun buildModel(): CreateOrderModel {
        return CreateOrderModel()
    }

    override fun fetchAddress() {
        if (!isDestroy) {
            view?.showLoading(false)
        }

        mModel?.let {
            addDispose(
                it.fetchAddress(1)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<AddressBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<AddressBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                val address = if (t != null && t!!.size >= 0) {
                                    t[0]
                                } else {
                                    null
                                }
                                view?.bindDefAddress(address)
                            }
                        }
                    })
            )
        }
    }

    override fun submitPurchaseOrder(
        productID: String,
        num: String,
        addressID: String,
        payType: String,
        password: String,
    ) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.submitPurchaseOrder(productID, num, addressID, payType, password)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<PayInfoBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: PayInfoBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                if ("1" == payType || "2" == payType) {
                                    view?.onSubmitOrderSuccess()
                                }  else {
                                    view?.onSubmitOrderSuccess()
                                }
                            }
                        }
                    })
            )
        }
    }
}