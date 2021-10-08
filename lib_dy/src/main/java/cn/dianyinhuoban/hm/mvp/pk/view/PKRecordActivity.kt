package cn.dianyinhuoban.hm.mvp.pk.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class PKRecordActivity : BaseActivity<IPresenter?>() {

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("PK记录")
        setContentView(R.layout.dy_activity_pk_record)
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, PKRecordFragment.newInstance(), "PKRecordFragment").commit()
    }
}