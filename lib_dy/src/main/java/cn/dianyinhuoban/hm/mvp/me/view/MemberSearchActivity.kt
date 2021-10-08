package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class MemberSearchActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_search)
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, MemberSearchFragment.newInstance(), "MemberSearchFragment")
            .commit()
    }
}