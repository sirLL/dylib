package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.mvp.bean.MemberDetailBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MemberInfoContract {
    interface Model {
        fun fetchMemberDetail(
            memberID: String,
            month: String,
        ): Observable<Response<MemberDetailBean?>>
    }

    interface Presenter {
        fun fetchMemberDetail(memberID: String, month: String)
    }

    interface View : IView {
        fun bindMemberDetail(memberDetail: MemberDetailBean?)
    }
}