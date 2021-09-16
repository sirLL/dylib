package cn.dianyinhuoban.hm.mvp.setting.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.HomeDataBean
import cn.dianyinhuoban.hm.mvp.bean.JiangWuTangBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface SettingContract {
    interface Model {
        fun getJiangWuTangList(page: Int): Observable<Response<MutableList<JiangWuTangBean>>>

        fun authApply(): Observable<Response<EmptyBean?>>

        fun fetchAuthResult():  Observable<Response<AuthResult?>>

        fun exit(): Observable<Response<EmptyBean?>>
    }

    interface Presenter {
        fun getJiangWuTangList(page: Int)

        fun authApply()


        fun fetchAuthResult()

        fun exit()

    }

    interface View : IView {

        fun onLoadJWTList(data: MutableList<JiangWuTangBean>) {}

        fun onApplySuccess() {}

        fun onApplyFail(errorMsg: String) {}

        fun bindAuthResult(data: AuthResult?) {}

        fun onExit() {}

    }

}