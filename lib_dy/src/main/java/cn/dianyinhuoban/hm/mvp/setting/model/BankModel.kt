package cn.dianyinhuoban.hm.mvp.setting.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.api.BankApiService
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.setting.contract.BankContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class BankModel : BaseModel(), BankContract.Model {

    override fun addBank(
        name: String,
        bankName: String,
        bankNo: String,
        phone: String,
        code: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(BankApiService::class.java)
            .addBank(name, bankName, bankNo, phone, code)
    }

    override fun sendSMS(
        phone: String,
        imageKey: String,
        imageCode: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .sendSMS(phone, imageCode, imageKey, "4")
    }

    override fun updateBank(
        name: String,
        bankName: String,
        bankNo: String,
        phone: String,
        code: String,
        id: Int
    ): Observable<Response<EmptyBean?>> {

        return mRetrofit.create(BankApiService::class.java)
            .updateBank(name, bankName, bankNo, phone, code, id);
    }

    override fun getBankList(): Observable<Response<List<BankBean>?>> {
        return mRetrofit.create(BankApiService::class.java)
            .getBankList()
    }

    override fun setBank(type: String, id: String): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(BankApiService::class.java).setBank(type, id)
    }

    override fun fetchImageCode(): Observable<Response<ImageCodeBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchImageCode()
    }

}