package cn.dianyinhuoban.hm.mvp.pk.model

import cn.dianyinhuoban.hm.api.PKApiService
import cn.dianyinhuoban.hm.mvp.bean.PKRecordBean
import cn.dianyinhuoban.hm.mvp.pk.contract.PKRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PKRecordModel : BaseModel(), PKRecordContract.Model {
    override fun fetchPKRecord(page: Int): Observable<Response<List<PKRecordBean>?>> {
        return mRetrofit.create(PKApiService::class.java)
            .fetchPKRecord(page)
    }
}