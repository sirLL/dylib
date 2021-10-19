package cn.dianyinhuoban.hm.mvp.income.contract

import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface WithdrawContract {
    interface Model {
        fun fetchPersonalData(): Observable<Response<PersonalBean?>>

        fun getBankList(): Observable<Response<List<BankBean>?>>

        fun getWithdrawFee(amount: String): Observable<Response<String?>>

        fun submitWithdraw(
            orderID: String,
            amount: String,
            payPassword: String
        ): Observable<Response<EmptyBean?>>
    }

    interface Presenter {
        fun fetchPersonalData()

        fun getBankList()

        fun getWithdrawFee(amount: String)

        fun submitWithdraw(
            bankCardID: String,
            amount: String,
            payPassword: String
        )
    }

    interface View : IView {
        fun bindPersonalData(personalBean: PersonalBean?)

        fun onLoadBankList(bankBeanList: List<BankBean>)

        fun onSubmitWithdrawSuccess()

        fun bindWithdrawFee(fee: String?)
    }
}