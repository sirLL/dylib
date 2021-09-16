package cn.dianyinhuoban.hm.api

import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface MessageApiService {

    @FormUrlEncoded
    @POST(URLConfig.URL_MESSAGE_LIST)
    fun getMessageList(@Field("page") page: Int, @Field("type") type: String,@Field("isFresh") isFresh: String): Observable<Response<MutableList<NotifyMessageBean>>>


    @FormUrlEncoded
    @POST(URLConfig.URL_MESSAGE_DETAIL)
    fun getMessageDetail(@Field("id") id: String): Observable<Response<NotifyMessageBean>>





}