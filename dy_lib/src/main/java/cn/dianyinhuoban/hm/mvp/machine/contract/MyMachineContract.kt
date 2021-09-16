package cn.dianyinhuoban.hm.mvp.machine.contract

import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.bean.TeamMachineItemBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MyMachineContract {
    interface Model {
        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            page: Int
        ): Observable<Response<MyMachineBean?>>

        fun fetchTeamMachine(
            name: String,
            page: Int
        ): Observable<Response<List<TeamMachineItemBean>?>>
    }

    interface Presenter {
        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            page: Int
        )

        fun fetchTeamMachine(
            name: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindMachine(data: MyMachineBean?)

        fun bindTeamMachine(data: List<TeamMachineItemBean>?)
    }

}