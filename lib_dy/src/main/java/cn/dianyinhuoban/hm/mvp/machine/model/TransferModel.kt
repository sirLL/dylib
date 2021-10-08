package cn.dianyinhuoban.hm.mvp.machine.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TransferModel : BaseModel(), TransferContract.Model {
    override fun submitMachineTransfer(
        receiverUID: String,
        machineType: String,
        isPay: String,
        price: String,
        machineIds: String,
        transferType: String,
        startMachineSN: String,
        endMachineSN: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitMachineTransfer(
                receiverUID,
                machineType,
                isPay,
                price,
                machineIds,
                transferType,
                startMachineSN,
                endMachineSN
            )
    }
}