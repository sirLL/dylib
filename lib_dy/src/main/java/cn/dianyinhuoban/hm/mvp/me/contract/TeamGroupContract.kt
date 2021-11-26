package cn.dianyinhuoban.hm.mvp.me.contract

import cn.dianyinhuoban.hm.bean.MemberLevelBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TeamGroupContract {
    interface Model : IModel {
        fun fetchMemberLevelList(): Observable<Response<List<MemberLevelBean?>?>>
    }

    interface Presenter : IPresenter {
        fun fetchMemberLevelList()
    }

    interface View : IView {
        fun bindMemberLevelList(data: List<MemberLevelBean?>?)
    }
}