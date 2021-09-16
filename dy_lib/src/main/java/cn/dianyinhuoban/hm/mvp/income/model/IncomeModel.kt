package cn.dianyinhuoban.hm.mvp.income.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.contract.IncomeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class IncomeModel : BaseModel(), IncomeContract.Model {
    override fun fetchIncome(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchPersonalData()
    }
}