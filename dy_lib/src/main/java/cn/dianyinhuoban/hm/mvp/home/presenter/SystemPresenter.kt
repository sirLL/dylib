package cn.dianyinhuoban.hm.mvp.home.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.SystemItemBean
import cn.dianyinhuoban.hm.mvp.home.contract.SystemContract
import cn.dianyinhuoban.hm.mvp.home.model.SystemModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class SystemPresenter(view: SystemContract.View) :
    BasePresenter<SystemModel, SystemContract.View>(view), SystemContract.Presenter {
    override fun buildModel(): SystemModel {
        return SystemModel()
    }

    override fun fetchSystemSetting() {
        mModel?.let {
            addDispose(
                it.fetchSystemSetting()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<SystemItemBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(systemData: List<SystemItemBean>) {
                            super.onNext(systemData)
                            if (!isDestroy) {
                                view?.bindSystemBean(systemData)
                            }
                        }
                    })
            )
        }
    }


}