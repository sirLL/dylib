package cn.dianyinhuoban.hm.mvp.income.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.WithdrawRecordBean
import cn.dianyinhuoban.hm.mvp.income.contract.WithdrawRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class WithdrawRecordModel : BaseModel(), WithdrawRecordContract.Model {
    override fun fetchWithdrawRecord(page: Int): Observable<Response<List<WithdrawRecordBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchWithdrawRecord(page)
    }
}