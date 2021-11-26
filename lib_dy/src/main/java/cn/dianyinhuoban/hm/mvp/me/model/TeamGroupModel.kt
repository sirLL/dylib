package cn.dianyinhuoban.hm.mvp.me.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.bean.MemberLevelBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamGroupContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TeamGroupModel : BaseModel(), TeamGroupContract.Model {
    override fun fetchMemberLevelList(): Observable<Response<List<MemberLevelBean?>?>> {
        return mRetrofit.create(ApiService::class.java).fetchMemberLevelList()
    }
}