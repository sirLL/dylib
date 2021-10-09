package com.example.hmdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.dianyinhuoban.hm.DYHelper
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.home.view.HomeActivity
import cn.dianyinhuoban.hm.mvp.login.view.LoginActivity

class MainActivity : AppCompatActivity() {
    private val userNameEditText: EditText by lazy {
        findViewById(R.id.ed_phone)
    }
    private val passwordEditText: EditText by lazy {
        findViewById(R.id.ed_password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_test).setOnClickListener {
            checkInput()
        }
    }

    private fun checkInput() {
        val userName = userNameEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this@MainActivity, "请输入用户名", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@MainActivity, "请输入登录密码", Toast.LENGTH_SHORT).show()
            return
        }
        login(userName, password)
    }

    //验证用户是否是原平台的用户
    private fun checkUserType(): Boolean {
        return false
    }

    private fun login(userName: String, password: String) {
        if (checkUserType()) {

        } else {
            DYHelper.login(userName, password, object : DYHelper.LoginCallback {
                override fun onLoginError(code: Int, msg: String?) {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }

                override fun onLoginSuccess(userBean: UserBean?) {
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }
            })
        }
    }
}