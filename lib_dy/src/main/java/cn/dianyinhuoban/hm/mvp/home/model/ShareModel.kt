package cn.dianyinhuoban.hm.mvp.home.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.ShareItemBean
import cn.dianyinhuoban.hm.mvp.home.contract.ShareContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class ShareModel : BaseModel(), ShareContract.Model {
    override fun fetchShareImage(): Observable<Response<List<ShareItemBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchShareImage()
    }
}