package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class MyClientActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_client)
        setTitle("我的客户")
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, MyClientFragment.newInstance(), "MyClientActivity").commit()
    }


}