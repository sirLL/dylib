package cn.dianyinhuoban.hm.mvp.setting.contract

import cn.dianyinhuoban.hm.mvp.bean.*
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface BankContract {
    interface Model {
        //添加银行卡
        fun addBank(name: String, bankName: String, bankNo: String, phone: String, code: String): Observable<Response<EmptyBean?>>

        //更新银行卡
        fun updateBank(name: String, bankName: String, bankNo: String, phone: String, code: String,id: Int): Observable<Response<EmptyBean?>>

        //获取银行卡列表
        fun getBankList(): Observable<Response<List<BankBean>?>>

        fun setBank( type: String,
                    id: String): Observable<Response<EmptyBean?>>


        fun fetchImageCode(): Observable<Response<ImageCodeBean?>>

        fun sendSMS(
            phone: String,
            imageKey: String,
            imageCode: String
        ): Observable<Response<EmptyBean?>>

    }

    interface Presenter {
        fun getBankList()

        //添加银行卡
        fun addBank(name: String, bankName: String, bankNo: String, phone: String, code: String)

        //更新银行卡
        fun updateBank(name: String, bankName: String, bankNo: String, phone: String, code: String,id: Int)

        //设置银行卡
        fun setBank(type: String, id: String)


        fun fetchImageCode()

        fun onSendSMS(phone: String, imageKey: String, imageCode: String)

    }

    interface View : IView {
        fun onAddBankSuccess() {}

        fun onUpdateBankSuccess() {}

        fun onLoadBankList(bankBeanList: List<BankBean>) {}

        fun onSetSuccess() {}


        fun showImageCode(imageCode: ImageCodeBean?)

        fun startSMSCountdown()

    }
}