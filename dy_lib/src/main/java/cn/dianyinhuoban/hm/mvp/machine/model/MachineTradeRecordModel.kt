package cn.dianyinhuoban.hm.mvp.machine.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.MachineTradeRecordBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineTradeRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MachineTradeRecordModel : BaseModel(), MachineTradeRecordContract.Model {
    override fun fetchTradeRecord(
        sn: String,
        page: Int
    ): Observable<Response<List<MachineTradeRecordBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchTradeRecord(sn, page)
    }
}