package cn.dianyinhuoban.hm.mvp.pk.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_pk.*

class PkActivity : BaseActivity<IPresenter?>() {

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_pk)
        tv_right.setOnClickListener {
            startActivity(Intent(PkActivity@ this, PKRecordActivity::class.java))
        }
        iv_back.setOnClickListener {
            finish()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.layout_pk_fragment_container, PersonalPkFragment.newInstance(), "team")
            .commit();

        tv_pk_personal.isSelected = true



        tv_pk_team.setOnClickListener {
            if (!it.isSelected) {
                tv_pk_personal.isSelected = false
                tv_pk_team.isSelected = true

                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.layout_pk_fragment_container,
                        TeamPkFragment.newInstance(),
                        "team"
                    )
                    .commit();
            }

        }

        tv_pk_personal.setOnClickListener {

            if (!it.isSelected) {

                tv_pk_personal.isSelected = true
                tv_pk_team.isSelected = false

                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.layout_pk_fragment_container,
                        PersonalPkFragment.newInstance(),
                        "personal"
                    )
                    .commit();
            }
        }

    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            val pkMember = data?.getParcelableExtra<PkMember>("data")
            Log.d("SSS2", "选择的是：" + pkMember?.teamName)
        }
    }

}