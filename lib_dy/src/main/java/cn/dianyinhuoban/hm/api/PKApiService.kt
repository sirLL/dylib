package cn.dianyinhuoban.hm.api

import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface PKApiService {

    @FormUrlEncoded
    @POST(URLConfig.URL_PK_LIST)
    fun getPkData(
        @Field("page") page: String,
        @Field("type") type: String,
    ): Observable<Response<PkDataBean>>

    @FormUrlEncoded
    @POST(URLConfig.URL_PK)
    fun doPk(
        @Field("type") type: String,
        @Field("acceptUid") acceptUid: String,
        @Field("cycle") cycle: String,
        @Field("reward") reward: String
    ): Observable<Response<EmptyBean?>>

    @FormUrlEncoded
    @POST(URLConfig.URL_PK_SEARCH)
    fun getPkMember(
        @Field("type") type: String,
        @Field("name") name: String,
        @Field("page") page: Int
    ): Observable<Response<MutableList<PkMember>>>

    @FormUrlEncoded
    @POST(URLConfig.URL_PK_RESPONSE)
    fun responsePK(
        @Field("status") status: String,
        @Field("id") id: String,
        @Field("peakId") peakId: String
    ): Observable<Response<EmptyBean?>>

    @FormUrlEncoded
    @POST(URLConfig.URL_PK_RECORD)
    fun fetchPKRecord(
        @Field("page") page: Int
    ): Observable<Response<List<PKRecordBean>?>>


}