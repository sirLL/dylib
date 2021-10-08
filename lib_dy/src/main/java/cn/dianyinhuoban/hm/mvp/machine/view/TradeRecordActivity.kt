package cn.dianyinhuoban.hm.mvp.machine.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TradeRecordActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_trade_record)
        setTitle("交易明细")
        val sn = intent?.extras?.getString("sn")
        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            TradeRecordFragment.newInstance(sn)
        ).commit()
    }


}