package cn.dianyinhuoban.hm.mvp.poster.contract

import cn.dianyinhuoban.hm.mvp.bean.PosterItemBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface PosterListContract {
    interface Model {
        fun fetchPosterList(
            typeID: String,
            page: Int
        ): Observable<Response<List<PosterItemBean>?>>
    }

    interface Presenter {
        fun fetchPosterList(typeID: String, page: Int)
    }

    interface View : IView {
        fun bindPosterList(data: List<PosterItemBean>)
    }
}