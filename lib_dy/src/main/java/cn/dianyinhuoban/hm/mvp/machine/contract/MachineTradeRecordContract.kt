package cn.dianyinhuoban.hm.mvp.machine.contract

import cn.dianyinhuoban.hm.mvp.bean.MachineTradeRecordBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MachineTradeRecordContract {
    interface Model {
        fun fetchTradeRecord(
            sn: String,
            page: Int
        ): Observable<Response<List<MachineTradeRecordBean>?>>
    }

    interface Presenter {
        fun fetchTradeRecord(
            sn: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindTradeRecord(data: List<MachineTradeRecordBean>?)
    }
}