package cn.dianyinhuoban.hm.mvp.me.view

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TeamActivity : BaseActivity<IPresenter?>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_team)
        setTitle("团队成员", Color.WHITE)
        supportFragmentManager.beginTransaction().add(R.id.fl_container, TeamFragment.newInstance(), "TeamFragment").commit()
    }

    override fun getStatusBarColor(): Int {
        return R.color.color_99027cfe
    }

    override fun getToolbarColor(): Int {
        return ContextCompat.getColor(this, R.color.color_99027cfe)
    }

    override fun getBackButtonIcon(): Int {
        return R.drawable.dy_base_ic_back_white;
    }


    override fun getPresenter(): IPresenter? {
        return null
    }
}