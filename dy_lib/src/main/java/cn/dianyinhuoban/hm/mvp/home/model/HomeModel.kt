package cn.dianyinhuoban.hm.mvp.home.model

import cn.dianyinhuoban.hm.api.ApiService
import cn.dianyinhuoban.hm.bean.CustomModel
import cn.dianyinhuoban.hm.mvp.bean.BannerBean
import cn.dianyinhuoban.hm.mvp.bean.HomeDataBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.home.contract.HomeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class HomeModel : BaseModel(),
    HomeContract.Model {
    override fun fetchHomeData(): Observable<Response<HomeDataBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchHomeData()
    }

    override fun fetchNoticeList(): Observable<Response<List<CustomModel>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchNoticeList()
    }

    override fun fetchDialogBanner(): Observable<Response<List<BannerBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchDialogBanner()
    }

    override fun fetchBanner(): Observable<Response<List<BannerBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchBannerList()
    }

    override fun fetchPersonalData(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchPersonalData()
    }

}