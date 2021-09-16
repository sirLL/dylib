package cn.dianyinhuoban.hm.api

import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface UploadService {

    @Multipart
    @POST(URLConfig.URL_UPLOAD_FILE)
    fun upload( @Part file: MultipartBody.Part): Observable<Response<UploadResultBean>>

}