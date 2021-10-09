package cn.dianyinhuoban.hm.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.MachineItemBean
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineManagerContract
import cn.dianyinhuoban.hm.mvp.machine.model.MachineManagerModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MachineManagerPresenter(view: MachineManagerContract.View) :
    BasePresenter<MachineManagerModel, MachineManagerContract.View>(view),
    MachineManagerContract.Presenter {
    override fun buildModel(): MachineManagerModel {
        return MachineManagerModel()
    }

    override fun fetchMachineType() {
        mModel?.let {
            addDispose(
                it.fetchMachineType()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<MachineTypeBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<MachineTypeBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMachineType(t)
                            }
                        }
                    })
            )
        }
    }

    override fun fetchMachine(type: String, status: String, sn: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchMachine(type, status, sn, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MyMachineBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: MyMachineBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMachine(t)
                            }
                        }
                    })
            )
        }
    }
}