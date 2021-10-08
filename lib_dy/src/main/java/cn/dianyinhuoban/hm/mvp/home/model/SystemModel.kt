package cn.dianyinhuoban.hm.mvp.home.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.SystemItemBean
import cn.dianyinhuoban.hm.mvp.home.contract.SystemContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class SystemModel : BaseModel(), SystemContract.Model {
    override fun fetchSystemSetting(): Observable<Response<List<SystemItemBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchSystemSetting()
    }


}