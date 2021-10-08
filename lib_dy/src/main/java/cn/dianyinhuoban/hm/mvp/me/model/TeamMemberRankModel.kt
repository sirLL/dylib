package cn.dianyinhuoban.hm.mvp.me.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.MemberRankBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberRankContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TeamMemberRankModel : BaseModel(), TeamMemberRankContract.Model {
    override fun fetchRank(
        page: Int
    ): Observable<Response<MemberRankBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMemberRank(page)
    }
}