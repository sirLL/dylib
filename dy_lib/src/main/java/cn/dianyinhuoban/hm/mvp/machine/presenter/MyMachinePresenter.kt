package cn.dianyinhuoban.hm.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.bean.TeamMachineItemBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MyMachineContract
import cn.dianyinhuoban.hm.mvp.machine.model.MyMachineModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MyMachinePresenter(view: MyMachineContract.View) :
    BasePresenter<MyMachineModel, MyMachineContract.View>(view), MyMachineContract.Presenter {
    override fun buildModel(): MyMachineModel {
        return MyMachineModel()
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

    override fun fetchTeamMachine(name: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchTeamMachine(name, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<TeamMachineItemBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<TeamMachineItemBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindTeamMachine(t)
                            }
                        }
                    })
            )
        }
    }
}