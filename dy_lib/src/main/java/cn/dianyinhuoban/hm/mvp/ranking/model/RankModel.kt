package cn.dianyinhuoban.hm.mvp.ranking.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.RankBean
import cn.dianyinhuoban.hm.mvp.ranking.contract.RankContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class RankModel : BaseModel(), RankContract.Model {
    override fun fetchRank(type: String, page: Int): Observable<Response<RankBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchRank(type, page)
    }
}