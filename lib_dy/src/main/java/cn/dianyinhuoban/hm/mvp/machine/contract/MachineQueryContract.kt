package cn.dianyinhuoban.hm.mvp.machine.contract

import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MachineQueryContract {
    interface Model {
        fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>>

        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            page: Int
        ): Observable<Response<MyMachineBean?>>

    }

    interface Presenter {
        fun fetchMachineType()

        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            page: Int
        )

        fun fetchMachineBySN(
            type: String,
            sn: String,
            page: Int,
            requestCode: Int
        )
    }

    interface View : IView {
        fun bindMachineType(data: List<MachineTypeBean>?)

        fun bindMachine(data: MyMachineBean?)

        fun bindMachineBySN(
            data: MyMachineBean?,
            requestCode: Int
        )
    }
}