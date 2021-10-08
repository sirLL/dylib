package cn.dianyinhuoban.hm.mvp.poster.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.PosterTypeBean
import cn.dianyinhuoban.hm.mvp.poster.contract.PosterTypeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PosterTypeModel : BaseModel(), PosterTypeContract.Model {
    override fun fetchPosterType(): Observable<Response<List<PosterTypeBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPosterType()
    }
}