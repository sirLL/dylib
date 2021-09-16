package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TeamMemberContract {
    interface Model {
        fun fetchTeamMember(
            type: String,
            keyWords: String,
            page: Int
        ): Observable<Response<List<TeamMemberBean>?>>
    }

    interface Presenter {
        fun fetchTeamMember(
            type: String,
            keyWords: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindMemberData(memberData: List<TeamMemberBean>)
    }
}