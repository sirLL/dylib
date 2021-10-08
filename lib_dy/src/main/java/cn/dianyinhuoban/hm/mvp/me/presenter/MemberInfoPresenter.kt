package cn.dianyinhuoban.hm.mvp.me.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.MemberDetailBean
import cn.dianyinhuoban.hm.mvp.me.contract.MemberInfoContract
import cn.dianyinhuoban.hm.mvp.me.model.MemberInfoModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MemberInfoPresenter(view: MemberInfoContract.View) :
    BasePresenter<MemberInfoModel, MemberInfoContract.View>(view), MemberInfoContract.Presenter {
    override fun buildModel(): MemberInfoModel {
        return MemberInfoModel()
    }

    override fun fetchMemberDetail(memberID: String, month: String) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.fetchMemberDetail(memberID, month)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MemberDetailBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: MemberDetailBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindMemberDetail(t)
                            }
                        }
                    })
            )
        }
    }
}