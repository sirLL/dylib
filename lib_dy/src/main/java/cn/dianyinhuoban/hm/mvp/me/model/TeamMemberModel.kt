package cn.dianyinhuoban.hm.mvp.me.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TeamMemberModel : BaseModel(), TeamMemberContract.Model {
    override fun fetchTeamMember(
        type: String,
        keyWords: String,
        page: Int
    ): Observable<Response<List<TeamMemberBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchTeamMember(type, keyWords, page)
    }
}