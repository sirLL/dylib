package cn.dianyinhuoban.hm.api

import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface ProfileApiService {

    /**
     * 修改个人信息
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_UPDATE_PROFILE)
    fun updateProfile(
        @Field("name") name: String,
        @Field("sex") sex: String,
        @Field("address") address: String,
        @Field("birthday") birthday: String,
        @Field("teamName") teamName: String,
    ): Observable<Response<EmptyBean?>>

    @POST(URLConfig.URL_PROFILE)
    fun getProfile(): Observable<Response<UserBean>>

    /**
     * 修改个人信息
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_UPDATE_AVATAR)
    fun updateAvatar(
        @Field("avatar") avatar: String
    ): Observable<Response<EmptyBean?>>

    /**
     * 添加/修改收货地址
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_SHIP_ADDRESS)
    fun submitShipAddress(
        @Field("name") name: String,
        @Field("provinceId") provinceId: String,
        @Field("cityId") cityId: String,
        @Field("districtId") districtId: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("id") id: String
    ): Observable<Response<String?>>
    /**
     * 修改登录密码
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_CHANGE_PASSWORD)
    fun updatePassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
        @Field("newPasswordConfirm") newPasswordConfirm: String
    ): Observable<Response<EmptyBean?>>












}