package cn.dianyinhuoban.hm.mvp.upload

import cn.dianyinhuoban.hm.mvp.bean.UploadResultBean
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import java.io.File

interface IUploadModel {

    fun upload(file: File): Observable<Response<UploadResultBean>>


}