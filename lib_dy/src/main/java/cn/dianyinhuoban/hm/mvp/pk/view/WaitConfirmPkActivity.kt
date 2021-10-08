package cn.dianyinhuoban.hm.mvp.pk.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class WaitConfirmPkActivity : BaseActivity<IPresenter?>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait_confirm_pk)


    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}