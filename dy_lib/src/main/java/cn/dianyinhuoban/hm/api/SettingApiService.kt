package cn.dianyinhuoban.hm.api

import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface SettingApiService {

    @FormUrlEncoded
    @POST(URLConfig.URL_JWT_LIST)
    fun getJWTList(@Field("page") page: Int): Observable<Response<MutableList<JiangWuTangBean>>>


    @POST(URLConfig.URL_AUTH_APPLY)
    fun authApply(): Observable<Response<EmptyBean?>>

    @POST(URLConfig.URL_EXIT)
    fun exit(): Observable<Response<EmptyBean?>>





}