package cn.dianyinhuoban.hm.mvp.machine.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.TransferRecordBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TransferRecordModel : BaseModel(), TransferRecordContract.Model {
    override fun fetchTransferRecord(page: Int): Observable<Response<List<TransferRecordBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchTransferRecord(page)
    }
}