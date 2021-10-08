package cn.dianyinhuoban.hm.mvp.machine.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TransferRecordActivity : BaseActivity<IPresenter?>() {

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("划拨明细")
        setContentView(R.layout.activity_transfer_record)
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, TransferRecordFragment.newInstance(), "TransferRecordFragment")
            .commit()
    }

}