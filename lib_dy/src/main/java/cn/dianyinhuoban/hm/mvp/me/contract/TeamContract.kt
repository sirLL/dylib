package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.mvp.bean.TeamInfoBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TeamContract {
    interface Model : IModel {
        fun fetchMyTeam(
                trans: String, regTime: String, groupId: String, page: Int, isAuth: String
        ): Observable<Response<TeamInfoBean?>>
    }

    interface Presenter : IPresenter {
        fun fetchMyTeam(
                trans: String, regTime: String, groupId: String, page: Int, isAuth: String
        )
    }

    interface View : IView {
        fun bindMyTeam(teamInfoBean: TeamInfoBean?)
    }
}