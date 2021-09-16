package cn.dianyinhuoban.hm.mvp.order

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity


class OrderResultActivity : BaseActivity<IPresenter?>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_result)

    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}