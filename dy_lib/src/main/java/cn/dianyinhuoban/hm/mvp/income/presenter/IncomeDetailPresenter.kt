package cn.dianyinhuoban.hm.mvp.income.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.IncomeDetailBean
import cn.dianyinhuoban.hm.mvp.income.contract.IncomeDetailContract
import cn.dianyinhuoban.hm.mvp.income.model.IncomeDetailModel

import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class IncomeDetailPresenter(view: IncomeDetailContract.View) :
    BasePresenter<IncomeDetailModel, IncomeDetailContract.View>(view),
    IncomeDetailContract.Presenter {
    override fun buildModel(): IncomeDetailModel {
        return IncomeDetailModel()
    }

    override fun fetchIncomeDetail(type: String, date: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchIncomeDetail(type, date, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<IncomeDetailBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: IncomeDetailBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindIncomeDetail(t)
                            }
                        }
                    })
            )
        }
    }
}