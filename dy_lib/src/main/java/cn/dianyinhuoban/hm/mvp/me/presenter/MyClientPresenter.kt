package cn.dianyinhuoban.hm.mvp.me.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.MyClientBean
import cn.dianyinhuoban.hm.mvp.me.contract.MyClientContract
import cn.dianyinhuoban.hm.mvp.me.model.MyClientModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MyClientPresenter(view: MyClientContract.View) :
    BasePresenter<MyClientModel, MyClientContract.View>(view), MyClientContract.Presenter {
    override fun buildModel(): MyClientModel {
        return MyClientModel()
    }

    override fun fetchMyClientBean(page: Int) {
        mModel?.let {
            addDispose(
                it.fetchMyClientBean(page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<MyClientBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<MyClientBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMyClient(t)
                            }
                        }
                    })
            )
        }

    }
}