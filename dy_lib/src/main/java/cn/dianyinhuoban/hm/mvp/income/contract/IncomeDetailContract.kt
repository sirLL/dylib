package cn.dianyinhuoban.hm.mvp.income.contract

import cn.dianyinhuoban.hm.mvp.bean.IncomeDetailBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface IncomeDetailContract {
    interface Model {
        fun fetchIncomeDetail(
            type: String,
            date: String,
            page: Int
        ): Observable<Response<IncomeDetailBean?>>
    }

    interface Presenter {
        fun fetchIncomeDetail(
            type: String,
            date: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindIncomeDetail(incomeDetail: IncomeDetailBean?)
    }
}