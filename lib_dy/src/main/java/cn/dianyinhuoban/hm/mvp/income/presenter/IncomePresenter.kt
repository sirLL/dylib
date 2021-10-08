package cn.dianyinhuoban.hm.mvp.income.presenter

import CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.contract.IncomeContract
import cn.dianyinhuoban.hm.mvp.income.model.IncomeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class IncomePresenter(view: IncomeContract.View) :
    BasePresenter<IncomeModel, IncomeContract.View>(view), IncomeContract.Presenter {
    override fun buildModel(): IncomeModel {
        return IncomeModel()
    }

    override fun fetchIncome() {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            it.fetchIncome()
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
                            view?.hideLoading()
                            view?.bindIncomeData(t)
                        }
                    }
                })
        }
    }
}