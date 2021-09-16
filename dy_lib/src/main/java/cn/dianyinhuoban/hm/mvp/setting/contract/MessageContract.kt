package cn.dianyinhuoban.hm.mvp.setting.contract

import cn.dianyinhuoban.hm.mvp.bean.NotifyMessageBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MessageContract {
    interface Model {
        //添加银行卡
        fun getMessageList(
            page: Int,
            type: String,
            isFresh: String
        ): Observable<Response<MutableList<NotifyMessageBean>>>


        //更新银行卡
        fun getMessageDetail(id: String): Observable<Response<NotifyMessageBean>>

    }

    interface Presenter {
        fun getMessageList(
            page: Int,
            type: String,
            isFresh: String
        )

        fun getMessageDetail(id: String)


    }

    interface View : IView {
        fun onLoadMessageList(data: MutableList<NotifyMessageBean>) {}
        fun onLoadMessageDetail(data: NotifyMessageBean) {}
    }
}