package cn.dianyinhuoban.hm.mvp.setting.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.setting.contract.ProfileContract
import cn.dianyinhuoban.hm.mvp.setting.model.ProfileModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider


class ProfilePresenter(view: ProfileContract.View) : BasePresenter<ProfileModel, ProfileContract.View>(view),

    ProfileContract.Presenter {
    override fun buildModel(): ProfileModel {
        return ProfileModel()
    }

    override fun updateTeamName(name: String) {}

    override fun updateProfile(
        name: String,
        sex: String,
        address: String,
        birthday: String,
        teamName: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.updateProfile(name, sex, address, birthday, teamName)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                            override fun onComplete() {
                                super.onComplete()
                                if (!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: EmptyBean) {
                                super.onNext(t)
                                if (!isDestroy)
                                    view?.hideLoading()
                                view?.onUpdateSuccess()
                            }
                        })
                )
            }
        }
    }

    override fun getProfile() {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.getProfile()
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<UserBean?>() {
                            override fun onComplete() {
                                super.onComplete()
                                if (!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: UserBean) {
                                super.onNext(t)
                                if (!isDestroy) {

                                    MMKVUtil.saveAvatar(t.avatar)
                                    MMKVUtil.saveNick(t.name)
                                    MMKVUtil.savePhone(t.phone)
                                    MMKVUtil.saveUserName(t.username)
                                    MMKVUtil.saveTeam(t.teamName)
                                    view?.onLoadProfile(t)
                                }
                            }
                        })
                )
            }
        }
    }

    override fun updateAvatar(avatar: String) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.uploadAvatar(avatar)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                            override fun onComplete() {
                                super.onComplete()
                                if (!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: EmptyBean) {
                                super.onNext(t)
                                if (!isDestroy)
                                    view?.onUpdateAvatar()
                            }
                        })
                )
            }
        }
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirm: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.changePassword(oldPassword,newPassword,newPasswordConfirm)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                            override fun onComplete() {
                                super.onComplete()
                                if (!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: EmptyBean) {
                                super.onNext(t)
                                if (!isDestroy)
                                    view?.onChangePasswordSuccess()
                            }
                        })
                )
            }
        }
    }


}