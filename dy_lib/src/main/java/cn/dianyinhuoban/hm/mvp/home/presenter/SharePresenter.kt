package cn.dianyinhuoban.hm.mvp.home.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.ShareItemBean
import cn.dianyinhuoban.hm.mvp.home.contract.ShareContract
import cn.dianyinhuoban.hm.mvp.home.model.ShareModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class SharePresenter(view: ShareContract.View) :
    BasePresenter<ShareModel, ShareContract.View>(view), ShareContract.Presenter {
    override fun buildModel(): ShareModel {
        return ShareModel()
    }

    override fun fetchShareImage() {
        mModel?.let {
            addDispose(
                it.fetchShareImage()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<ShareItemBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(data: List<ShareItemBean>) {
                            super.onNext(data)
                            if (!isDestroy) {
                                view?.bindShareImage(data)
                            }
                        }
                    })
            )
        }
    }
}