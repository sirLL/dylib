package cn.dianyinhuoban.hm.mvp.ranking.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.RankBean
import cn.dianyinhuoban.hm.mvp.bean.RankItemBean
import cn.dianyinhuoban.hm.mvp.ranking.contract.RankContract
import cn.dianyinhuoban.hm.mvp.ranking.model.RankModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class RankPresenter(view: RankContract.View) : BasePresenter<RankModel, RankContract.View>(view),
    RankContract.Presenter {
    override fun buildModel(): RankModel {
        return RankModel()
    }

    override fun fetchRank(type: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchRank(type, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<RankBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: RankBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindRankData(t)
                            }
                        }
                    })
            )
        }
    }
}