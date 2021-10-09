package com.example.hmdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cn.dianyinhuoban.hm.DYHelper
import com.example.hmdemo.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            openPage()
        }, 2000)
    }

    private fun openPage() {
        if (DYHelper.hasLoggedDYHM()) {
            //登录过电银泓盟版APP,直接跳转电银泓盟版APP
            DYHelper.openDYHM(this)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}