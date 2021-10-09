package cn.dianyinhuoban.hm.mvp.me.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.MemberRankBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberRankContract
import cn.dianyinhuoban.hm.mvp.me.model.TeamMemberRankModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TeamMemberRankPresenter(view: TeamMemberRankContract.View) :
    BasePresenter<TeamMemberRankModel,
            TeamMemberRankContract.View>(view), TeamMemberRankContract.Presenter {
    override fun buildModel(): TeamMemberRankModel {
        return TeamMemberRankModel()
    }

    override fun fetchRank(page: Int) {
        mModel?.let {
            addDispose(
                it.fetchRank(page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MemberRankBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: MemberRankBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindRank(t)
                            }
                        }
                    })
            )
        }
    }
}