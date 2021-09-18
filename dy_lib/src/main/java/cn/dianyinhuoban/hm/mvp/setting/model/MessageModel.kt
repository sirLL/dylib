package cn.dianyinhuoban.hm.mvp.setting.model

import cn.dianyinhuoban.hm.api.MessageApiService
import cn.dianyinhuoban.hm.mvp.bean.*
import cn.dianyinhuoban.hm.mvp.setting.contract.MessageContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MessageModel : BaseModel(), MessageContract.Model {


    override fun getMessageList(
        page: Int,
        type: String,
        isFresh: String
    ): Observable<Response<MutableList<NotifyMessageBean>>> {
        return mRetrofit.create(MessageApiService::class.java).getMessageList(page, type, isFresh)
    }

    override fun getMessageDetail(id: String): Observable<Response<NotifyMessageBean>> {
        return mRetrofit.create(MessageApiService::class.java).getMessageDetail(id)
    }


}