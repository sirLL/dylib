package cn.dianyinhuoban.hm.mvp.income.contract

import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface IncomeContract {
    interface Model {
        fun fetchIncome(): Observable<Response<PersonalBean?>>
    }

    interface Presenter {
        fun fetchIncome()
    }

    interface View : IView {
        fun bindIncomeData(personalBean: PersonalBean?)
    }
}