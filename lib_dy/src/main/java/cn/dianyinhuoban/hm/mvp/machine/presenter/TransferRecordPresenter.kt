package cn.dianyinhuoban.hm.mvp.machine.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.TransferRecordBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferRecordContract
import cn.dianyinhuoban.hm.mvp.machine.model.TransferRecordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TransferRecordPresenter(view: TransferRecordContract.View) :
    BasePresenter<TransferRecordModel, TransferRecordContract.View>(view),
    TransferRecordContract.Presenter {
    override fun buildModel(): TransferRecordModel {
        return TransferRecordModel()
    }

    override fun fetchTransferRecord(page: Int) {
        mModel?.let {
            addDispose(
                it.fetchTransferRecord(page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<TransferRecordBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<TransferRecordBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindTransferRecord(t)
                            }
                        }
                    })
            )
        }
    }
}