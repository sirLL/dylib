package cn.dianyinhuoban.hm.mvp.machine.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class BatchTransferActivity : BaseActivity<IPresenter?>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_batch_transfer)


    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}