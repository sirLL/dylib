package cn.dianyinhuoban.hm.mvp.setting.model

import cn.dianyinhuoban.hm.api.SettingApiService
import cn.dianyinhuoban.hm.mvp.bean.*
import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.setting.contract.SettingContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class SettingModel : BaseModel(), SettingContract.Model {


    override fun getJiangWuTangList(page: Int): Observable<Response<MutableList<JiangWuTangBean>>> {
        return mRetrofit.create(SettingApiService::class.java).getJWTList(page)
    }

    override fun authApply(): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(SettingApiService::class.java)
            .authApply()
    }

    override fun fetchAuthResult(): Observable<Response<AuthResult?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAuthResult()
    }

    override fun exit(): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(SettingApiService::class.java)
            .exit()
    }
}