package cn.dianyinhuoban.hm.mvp.income.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.api.BankApiService
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.contract.WithdrawContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class WithdrawModel : BaseModel(), WithdrawContract.Model {
    override fun fetchPersonalData(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPersonalData()
    }

    override fun getBankList(): Observable<Response<List<BankBean>?>> {
        return mRetrofit.create(BankApiService::class.java)
            .getBankList()
    }

    override fun submitWithdraw(
        bankCardID: String,
        amount: String,
        payPassword: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitWithdraw(bankCardID, amount, payPassword)
    }
}