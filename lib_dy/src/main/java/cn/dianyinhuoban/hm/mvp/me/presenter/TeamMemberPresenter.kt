package cn.dianyinhuoban.hm.mvp.me.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberContract
import cn.dianyinhuoban.hm.mvp.me.model.TeamMemberModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TeamMemberPresenter(view: TeamMemberContract.View) :
    BasePresenter<TeamMemberModel,
            TeamMemberContract.View>(view), TeamMemberContract.Presenter {
    override fun buildModel(): TeamMemberModel {
        return TeamMemberModel()
    }

    override fun fetchTeamMember(type: String, keyWords: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchTeamMember(type, keyWords, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<TeamMemberBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<TeamMemberBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMemberData(t)
                            }
                        }
                    })
            )
        }
    }
}