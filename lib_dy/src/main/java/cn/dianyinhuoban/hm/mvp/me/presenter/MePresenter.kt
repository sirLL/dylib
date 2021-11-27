package cn.dianyinhuoban.hm.mvp.me.presenter

import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.me.contract.MeContract
import cn.dianyinhuoban.hm.mvp.me.model.MeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MePresenter(view: MeContract.View) : BasePresenter<MeModel, MeContract.View>(view),
    MeContract.Presenter {
    override fun buildModel(): MeModel {
        return MeModel()
    }

    override fun fetchPersonalData() {
        mModel?.let {
            it.fetchPersonalData()
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<PersonalBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: PersonalBean) {
                        super.onNext(t)
                        MMKVUtil.saveUserID(t.uid)
                        MMKVUtil.saveUserName(t.username)
                        MMKVUtil.saveNick(t.name)
                        MMKVUtil.saveAvatar(t.avatar)
                        MMKVUtil.saveTeam(t.teamName)
                        MMKVUtil.saveIsTeamLeader(t?.isTeamLeader == 2)
                        if (!isDestroy) {
                            view?.bindPersonalData(t)
                        }
                    }
                })
        }
    }

    override fun fetchAuthResult() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.fetchAuthResult()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<AuthResult?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: AuthResult) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindAuthResult(t)
                            }
                        }
                    })
            )
        }
    }
}