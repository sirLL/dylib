package cn.dianyinhuoban.hm.mvp.machine.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.TransferRecordDetailBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferRecordDetailContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TransferRecordDetailModel : BaseModel(), TransferRecordDetailContract.Model {
    override fun fetchTransferRecordDetail(
        recordID: String,
        page: Int
    ): Observable<Response<List<TransferRecordDetailBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchTransferRecordDetail(recordID, page)
    }
}