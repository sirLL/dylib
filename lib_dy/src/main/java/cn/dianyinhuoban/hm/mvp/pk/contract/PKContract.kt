package cn.dianyinhuoban.hm.mvp.pk.contract

import cn.dianyinhuoban.hm.mvp.bean.EmptyBean
import cn.dianyinhuoban.hm.mvp.bean.PkDataBean
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface PKContract {

    interface Model {

        fun getPKData(page: Int, type: Int): Observable<Response<PkDataBean>>

        fun launchPk(type: String, acceptUid: String, cycle: String, reward: String): Observable<Response<EmptyBean?>>

        fun getPkMember(type: String, name: String, page: Int): Observable<Response<MutableList<PkMember>>>

        fun responsePK(status: String,id: String,peakId:String): Observable<Response<EmptyBean?>>

    }

    interface Presenter {

        fun getPKData(page: Int, type: Int)

        fun launchPk(type: String, acceptUid: String, cycle: String, reward: String)

        fun getPkMember(type: String,name: String, page: Int)

        fun responsePK(status: String,id: String,peakId:String)

    }

    interface View : IView {

        fun onLoadPKData(data: PkDataBean) {}

        fun onLaunchPkSuccess(){}

        fun onLoadPkMember(data: MutableList<PkMember>){}

        fun onResponsePKSuccess(){}

    }
}