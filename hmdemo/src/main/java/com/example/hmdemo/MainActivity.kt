package com.example.hmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import cn.dianyinhuoban.hm.DYHelper

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
        findViewById<ImageView>(R.id.iv_logo).setOnClickListener {
            DYHelper.openLoginPage(this@MainActivity, true)
        }
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

    private fun login(userName: String, password: String) {

    }
}