package cn.dianyinhuoban.hm.mvp.machine.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineQueryContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MachineQueryModel : BaseModel(), MachineQueryContract.Model {
    override fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMachineType()
    }

    override fun fetchMachine(
        type: String,
        status: String,
        sn: String,
        page: Int
    ): Observable<Response<MyMachineBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMyMachine(type, status, sn, page)
    }
}