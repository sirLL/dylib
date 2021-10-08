package cn.dianyinhuoban.hm.mvp.home.contract

import cn.dianyinhuoban.hm.bean.CustomModel
import cn.dianyinhuoban.hm.mvp.bean.BannerBean
import cn.dianyinhuoban.hm.mvp.bean.HomeDataBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface HomeContract {
    interface Model {
        fun fetchHomeData(): Observable<Response<HomeDataBean?>>

        fun fetchNoticeList(): Observable<Response<List<CustomModel>?>>

        fun fetchDialogBanner(): Observable<Response<List<BannerBean>?>>

        fun fetchBanner(): Observable<Response<List<BannerBean>?>>

        fun fetchPersonalData(): Observable<Response<PersonalBean?>>
    }

    interface Presenter {
        fun fetchHomeData()

        fun fetchNoticeList()

        fun fetchDialogBanner()

        fun fetchBanner()

        fun fetchPersonalData()
    }

    interface View : IView {
        fun bindHomeData(homeDataBean: HomeDataBean?)

        fun bindNoticeList(data: List<CustomModel>?)

        fun bindDialogBanner(data: List<BannerBean>?)

        fun bindBanner(data: List<BannerBean>?)

        fun bindPersonalData(personalBean: PersonalBean?)
    }
}