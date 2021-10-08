package cn.dianyinhuoban.hm.mvp.pk.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class PkResultActivity : BaseActivity<IPresenter?>() {

    companion object {

        fun open(context: Context) {
            var intent = Intent(context, PkResultActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pk_result)

    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }


    override fun getPresenter(): IPresenter? {
        return null
    }
}