package cn.dianyinhuoban.hm.mvp.upload

import cn.dianyinhuoban.hm.api.UploadService
import cn.dianyinhuoban.hm.mvp.bean.UploadResultBean
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class FileModel : BaseModel(), IUploadModel {

    override fun upload(file: File): Observable<Response<UploadResultBean>> {

        val parse = "multipart/form-data"

        val requestFile: RequestBody = RequestBody.create(MediaType.parse(parse), file)

        val multipartBody: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)

        return mRetrofit.create(UploadService::class.java).upload(multipartBody)
    }


}