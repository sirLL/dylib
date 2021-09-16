package cn.dianyinhuoban.hm.mvp.income.contract

import cn.dianyinhuoban.hm.mvp.bean.WithdrawRecordBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface WithdrawRecordContract {
    interface Model {
        fun fetchWithdrawRecord(page: Int): Observable<Response<List<WithdrawRecordBean>?>>
    }

    interface Presenter {
        fun fetchWithdrawRecord(page: Int)
    }

    interface View :IView{
        fun bindWithdrawRecord(recordData: List<WithdrawRecordBean>?)
    }
}