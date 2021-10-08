package cn.dianyinhuoban.hm.mvp.machine.contract

import cn.dianyinhuoban.hm.mvp.bean.TransferRecordBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TransferRecordContract {
    interface Model {
        fun fetchTransferRecord(page: Int): Observable<Response<List<TransferRecordBean>?>>
    }

    interface Presenter {
        fun fetchTransferRecord(page: Int)
    }

    interface View : IView {
        fun bindTransferRecord(data: List<TransferRecordBean>?)
    }
}