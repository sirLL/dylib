package cn.dianyinhuoban.hm.mvp.income.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.income.contract.BankCardListContract
import cn.dianyinhuoban.hm.mvp.income.model.BankCardListModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class BankCardListPresenter(view: BankCardListContract.View) :
    BasePresenter<BankCardListModel, BankCardListContract.View>(view),
    BankCardListContract.Presenter {
    override fun buildModel(): BankCardListModel {
        return BankCardListModel()
    }

    override fun getBankList() {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.getBankList()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<BankBean>?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: List<BankBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onLoadBankList(t)
                            }
                        }
                    })
            )
        }

    }
}