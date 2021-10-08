package cn.dianyinhuoban.hm.mvp.setting.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.HomeDataBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface ProfileContract {
    interface Model {
        fun updateProfile(name: String, sex: String, address: String, birthday: String, teamName: String): Observable<Response<EmptyBean?>>

        fun updateTeamName(name: String)

        fun getProfile(): Observable<Response<UserBean>>

        fun uploadAvatar(avatar: String): Observable<Response<EmptyBean?>>

        fun changePassword(oldPassword: String, newPassword: String, newPasswordConfirm: String): Observable<Response<EmptyBean?>>


    }

    interface Presenter {

        fun updateTeamName(name: String)

        fun updateProfile(name: String, sex: String, address: String, birthday: String, teamName: String)

        fun getProfile()

        fun updateAvatar(avatar: String)

        fun changePassword(oldPassword: String, newPassword: String, newPasswordConfirm: String)
    }

    interface View : IView {
        fun onUpdateSuccess() {}

        fun onLoadProfile(profile: UserBean) {}

        fun onUpdateAvatar() {}

        fun onChangePasswordSuccess() {}


    }
}