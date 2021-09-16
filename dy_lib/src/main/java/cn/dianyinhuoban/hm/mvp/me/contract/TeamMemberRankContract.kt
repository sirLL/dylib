package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.mvp.bean.MemberRankBean
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TeamMemberRankContract {
    interface Model {
        fun fetchRank(
            page: Int
        ): Observable<Response<MemberRankBean?>>
    }

    interface Presenter {
        fun fetchRank(
            page: Int
        )
    }

    interface View : IView {
        fun bindRank(data: MemberRankBean?)
    }
}