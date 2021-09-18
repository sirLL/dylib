package cn.dianyinhuoban.hm.mvp.machine.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TransferRecordDetailActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_transfer_record_detail)
        setTitle("划拨详情")
        val recordID = intent?.extras?.getString("recordID", "")
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fl_container,
                TransferRecordDetailFragment.newInstance(recordID),
                "TransferRecordDetailFragment"
            ).commit()
    }
}