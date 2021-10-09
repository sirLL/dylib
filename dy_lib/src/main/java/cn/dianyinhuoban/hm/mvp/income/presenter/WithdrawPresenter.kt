package cn.dianyinhuoban.hm.mvp.income.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.contract.WithdrawContract
import cn.dianyinhuoban.hm.mvp.income.model.WithdrawModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class WithdrawPresenter(view: WithdrawContract.View) :
    BasePresenter<WithdrawModel, WithdrawContract.View>(view), WithdrawContract.Presenter {
    override fun buildModel(): WithdrawModel {
        return WithdrawModel()
    }

    override fun fetchPersonalData() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
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
                            if (!isDestroy) {
                                view?.bindPersonalData(t)
                            }
                        }
                    })
            )
        }
    }

    override fun getBankList() {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.getBankList()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<BankBean>?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: List<BankBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onLoadBankList(t)
                            }
                        }
                    })
            )
        }
    }

    override fun submitWithdraw(bankCardID: String, amount: String, payPassword: String) {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.submitWithdraw(bankCardID, amount, payPassword)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: EmptyBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onSubmitWithdrawSuccess()
                            }
                        }
                    })
            )
        }
    }
}