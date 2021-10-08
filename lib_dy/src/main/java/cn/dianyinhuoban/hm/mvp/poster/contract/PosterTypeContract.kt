package cn.dianyinhuoban.hm.mvp.poster.contract

import cn.dianyinhuoban.hm.mvp.bean.PosterTypeBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface PosterTypeContract {
    interface Model {
        fun fetchPosterType(): Observable<Response<List<PosterTypeBean>?>>
    }

    interface Presenter {
        fun fetchPosterType()
    }

    interface View : IView {
        fun bindPosterType(data: List<PosterTypeBean>?)
    }
}