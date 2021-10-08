package cn.dianyinhuoban.hm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import cn.dianyinhuoban.hm.mvp.home.view.HomeActivity
import cn.dianyinhuoban.hm.mvp.login.view.LoginActivity
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.cache.MMKVUtil

class SplashActivity : BaseActivity<IPresenter?>() {

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            openPage()
        }, 2000)
    }

    private fun openPage() {
        val uid = MMKVUtil.getUserID()
        val userName = MMKVUtil.getUserName()
        val token = MMKVUtil.getToken()
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(token)) {
            startActivity(Intent(SplashActivity@ this, HomeActivity::class.java))
        } else {
            startActivity(Intent(SplashActivity@ this, LoginActivity::class.java))
        }
        finish()
    }

}