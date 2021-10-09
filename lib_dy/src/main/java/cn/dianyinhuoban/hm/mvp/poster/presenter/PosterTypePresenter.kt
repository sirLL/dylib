package cn.dianyinhuoban.hm.mvp.poster.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.PosterTypeBean
import cn.dianyinhuoban.hm.mvp.poster.contract.PosterTypeContract
import cn.dianyinhuoban.hm.mvp.poster.model.PosterTypeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class PosterTypePresenter(view: PosterTypeContract.View) :
    BasePresenter<PosterTypeModel, PosterTypeContract.View>(view), PosterTypeContract.Presenter {
    override fun buildModel(): PosterTypeModel {
        return PosterTypeModel()
    }

    override fun fetchPosterType() {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.fetchPosterType()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<PosterTypeBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<PosterTypeBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindPosterType(t)
                            }
                        }
                    })
            )
        }
    }
}