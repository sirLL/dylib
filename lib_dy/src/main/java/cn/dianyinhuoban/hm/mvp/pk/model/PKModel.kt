package cn.dianyinhuoban.hm.mvp.pk.model

import cn.dianyinhuoban.hm.api.PKApiService
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PkDataBean
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import cn.dianyinhuoban.hm.mvp.pk.contract.PKContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PKModel : BaseModel(), PKContract.Model {

    override fun getPKData(page: Int, type: Int): Observable<Response<PkDataBean>> {
        return mRetrofit.create(PKApiService::class.java)
            .getPkData(page.toString(), type.toString())
    }

    override fun launchPk(
        type: String,
        acceptUid: String,
        cycle: String,
        reward: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(PKApiService::class.java).doPk(type,acceptUid,cycle,reward)
    }

    override fun getPkMember(
        type: String,
        name: String,
        page: Int
    ): Observable<Response<MutableList<PkMember>>> {
        return mRetrofit.create(PKApiService::class.java).getPkMember(type,name,page)
    }

    override fun responsePK(
        status: String,
        id: String,
        peakId: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(PKApiService::class.java).responsePK(status,id, peakId)
    }

}