package cn.dianyinhuoban.hm.mvp.me.presenter

import cn.dianyinhuoban.hm.bean.MemberLevelBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamGroupContract
import cn.dianyinhuoban.hm.mvp.me.model.TeamGroupModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TeamGroupPresenter(view: TeamGroupContract.View) : BasePresenter<TeamGroupContract.Model, TeamGroupContract.View>(view), TeamGroupContract.Presenter {
    override fun buildModel(): TeamGroupContract.Model {
        return TeamGroupModel()
    }

    override fun fetchMemberLevelList() {
        mModel?.let {
            if (!isDestroy) {
                view?.showLoading()
            }
            addDispose(
                    it.fetchMemberLevelList()
                            .compose(SchedulerProvider.getInstance().applySchedulers())
                            .compose(ResponseTransformer.handleResult())
                            .subscribeWith(object : CustomResourceSubscriber<List<MemberLevelBean?>?>() {
                                override fun onError(exception: ApiException?) {
                                    handleError(exception)
                                }

                                override fun onNext(t: List<MemberLevelBean?>) {
                                    if (!isDestroy) {
                                        view?.hideLoading()
                                        view?.bindMemberLevelList(t)
                                    }
                                }
                            })
            )
        }
    }
}