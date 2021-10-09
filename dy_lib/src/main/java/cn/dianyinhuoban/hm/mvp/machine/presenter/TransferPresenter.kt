package cn.dianyinhuoban.hm.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferContract
import cn.dianyinhuoban.hm.mvp.machine.model.TransferModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TransferPresenter(view: TransferContract.View) :
    BasePresenter<TransferModel, TransferContract.View>(view), TransferContract.Presenter {
    override fun buildModel(): TransferModel {
        return TransferModel()
    }

    override fun submitMachineTransfer(
        receiverUID: String,
        machineType: String,
        isPay: String,
        price: String,
        machineIds: String,
        transferType: String,
        startMachineSN: String,
        endMachineSN: String
    ) {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.submitMachineTransfer(
                    receiverUID,
                    machineType,
                    isPay,
                    price,
                    machineIds,
                    transferType,
                    startMachineSN,
                    endMachineSN
                )
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
                                view?.onSubmitMachineTransferSuccess()
                            }
                        }
                    })
            )
        }
    }
}