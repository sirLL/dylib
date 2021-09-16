package cn.dianyinhuoban.hm.mvp.setting.view

import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class JiangWuTangActivity : BaseActivity<IPresenter?>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jiang_wu_tang)
        setTitle("讲武堂")

        supportFragmentManager.beginTransaction()
            .add(R.id.el_container,JiangWuTangFragment(),"jwt")
            .commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}