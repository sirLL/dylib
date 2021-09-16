package cn.dianyinhuoban.hm.mvp.setting.model

import cn.dianyinhuoban.hm.api.ProfileApiService
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.setting.contract.ProfileContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class ProfileModel : BaseModel(), ProfileContract.Model {

    override fun updateProfile(
        name: String,
        sex: String,
        address: String,
        birthday: String,
        teamName: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ProfileApiService::class.java)
            .updateProfile(name, sex, address, birthday, teamName)
    }

    override fun updateTeamName(name: String) {

    }

    override fun getProfile(): Observable<Response<UserBean>> {
        return mRetrofit
            .create(ProfileApiService::class.java)
            .getProfile()
    }

    override fun uploadAvatar(avatar: String): Observable<Response<EmptyBean?>> {
        return mRetrofit
            .create(ProfileApiService::class.java)
            .updateAvatar(avatar)
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirm: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ProfileApiService::class.java)
            .updatePassword(oldPassword,newPassword,newPasswordConfirm)
    }
}