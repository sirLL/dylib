package cn.dianyinhuoban.hm.mvp.poster.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.PosterItemBean
import cn.dianyinhuoban.hm.mvp.poster.contract.PosterListContract
import cn.dianyinhuoban.hm.mvp.poster.model.PosterListModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class PosterListPresenter(view: PosterListContract.View) :
    BasePresenter<PosterListModel, PosterListContract.View>(view), PosterListContract.Presenter {
    override fun buildModel(): PosterListModel {
        return PosterListModel()
    }

    override fun fetchPosterList(typeID: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchPosterList(typeID, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<PosterItemBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<PosterItemBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindPosterList(t)
                            }
                        }
                    })
            )
        }
    }
}