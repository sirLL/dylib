package cn.dianyinhuoban.hm.mvp.me.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.MyClientBean
import cn.dianyinhuoban.hm.mvp.me.contract.MyClientContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MyClientModel : BaseModel(), MyClientContract.Model {
    override fun fetchMyClientBean(page: Int): Observable<Response<List<MyClientBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMyClientBean(page)
    }
}