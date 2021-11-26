package cn.dianyinhuoban.hm.mvp.me.presenter

import cn.dianyinhuoban.hm.mvp.bean.TeamInfoBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamContract
import cn.dianyinhuoban.hm.mvp.me.model.TeamModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TeamPresenter(view: TeamContract.View) : BasePresenter<TeamContract.Model, TeamContract.View>(view), TeamContract.Presenter {
    override fun buildModel(): TeamContract.Model {
        return TeamModel()
    }

    override fun fetchMyTeam(trans: String, regTime: String, groupId: String, page: Int, isAuth: String) {
        mModel?.let {
            addDispose(
                    it.fetchMyTeam(trans, regTime, groupId, page, isAuth)
                            .compose(SchedulerProvider.getInstance().applySchedulers())
                            .compose(ResponseTransformer.handleResult())
                            .subscribeWith(object : CustomResourceSubscriber<TeamInfoBean?>() {
                                override fun onError(exception: ApiException?) {
                                    handleError(exception)
                                }

                                override fun onNext(t: TeamInfoBean) {
                                    super.onNext(t)
                                    if (!isDestroy) {
                                        view?.bindMyTeam(t)
                                    }
                                }
                            })
            )
        }
    }
}