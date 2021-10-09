package cn.dianyinhuoban.hm.mvp.pk.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.PKRecordBean
import cn.dianyinhuoban.hm.mvp.pk.contract.PKRecordContract
import cn.dianyinhuoban.hm.mvp.pk.model.PKRecordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class PKRecordPresenter(view: PKRecordContract.View) :
    BasePresenter<PKRecordModel, PKRecordContract.View>(view), PKRecordContract.Presenter {
    override fun buildModel(): PKRecordModel {
        return PKRecordModel()
    }

    override fun fetchPKRecord(page: Int) {
        mModel?.let {
            addDispose(
                it.fetchPKRecord(page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<PKRecordBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<PKRecordBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindPKRecord(t)
                            }
                        }
                    })
            )
        }
    }
}