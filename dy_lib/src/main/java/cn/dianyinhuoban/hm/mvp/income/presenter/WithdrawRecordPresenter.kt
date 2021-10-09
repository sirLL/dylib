package cn.dianyinhuoban.hm.mvp.income.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.WithdrawRecordBean
import cn.dianyinhuoban.hm.mvp.income.contract.WithdrawRecordContract
import cn.dianyinhuoban.hm.mvp.income.model.WithdrawRecordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class WithdrawRecordPresenter(view: WithdrawRecordContract.View) :
    BasePresenter<WithdrawRecordModel, WithdrawRecordContract.View>(view),
    WithdrawRecordContract.Presenter {
    override fun buildModel(): WithdrawRecordModel {
        return WithdrawRecordModel()
    }

    override fun fetchWithdrawRecord(page: Int) {
        if (!isDestroy) {
            mModel?.let {
                addDispose(
                    it.fetchWithdrawRecord(page)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object :
                            CustomResourceSubscriber<List<WithdrawRecordBean>?>() {
                            override fun onError(exception: ApiException?) {
                                handleError(exception)
                            }

                            override fun onNext(t: List<WithdrawRecordBean>) {
                                super.onNext(t)
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    view?.bindWithdrawRecord(t)
                                }
                            }
                        })
                )
            }

        }
    }
}