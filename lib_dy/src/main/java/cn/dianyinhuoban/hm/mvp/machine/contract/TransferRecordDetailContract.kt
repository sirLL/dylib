package cn.dianyinhuoban.hm.mvp.machine.contract

import cn.dianyinhuoban.hm.mvp.bean.TransferRecordDetailBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TransferRecordDetailContract {
    interface Model {
        fun fetchTransferRecordDetail(
            recordID: String,
            page: Int
        ): Observable<Response<List<TransferRecordDetailBean>?>>
    }

    interface Presenter {
        fun fetchTransferRecordDetail(
            recordID: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindRecordDetail(data: List<TransferRecordDetailBean>?)
    }
}