package cn.dianyinhuoban.hm

import com.hjq.toast.ToastUtils
import com.wareroom.lib_base.BaseApplication
import com.wareroom.lib_base.net.RetrofitServiceManager


class DYApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
        RetrofitServiceManager.initialize(this)
    }
}