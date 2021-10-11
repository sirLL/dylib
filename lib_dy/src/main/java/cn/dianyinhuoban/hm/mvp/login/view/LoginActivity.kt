package cn.dianyinhuoban.hm.mvp.login.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import cn.dianyinhuoban.hm.DYHelper
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.event.CloseLoadingEvent
import cn.dianyinhuoban.hm.event.CloseLoginPageEvent
import cn.dianyinhuoban.hm.mvp.home.view.HomeActivity
import cn.dianyinhuoban.hm.mvp.login.contract.LoginContract
import cn.dianyinhuoban.hm.mvp.login.presenter.LoginPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import kotlinx.android.synthetic.main.dy_activity_login.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : BaseActivity<LoginPresenter?>(), LoginContract.View {
    private var showBackBtn = false
    override fun getPresenter(): LoginPresenter? {
        return LoginPresenter(this)
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.dy_activity_login)
        initView()
        val defPhone = MMKVUtil.getUserName()
        val defPassword = MMKVUtil.getLoginPassword()
        ed_phone.setText(defPhone)
        ed_password.setText(defPassword)
        ed_phone.setSelection(defPhone.length)
        ed_password.setSelection(defPassword.length)
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        bundle?.let {
             showBackBtn = it.getBoolean("showBackBtn", false)
        }
    }

    private fun initView() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                setSubmitButtonEnable()
            }
        }
        iv_back.visibility = if (showBackBtn) View.VISIBLE else View.GONE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        ed_phone.addTextChangedListener(textWatcher)
        ed_password.addTextChangedListener(textWatcher)
        iv_eye.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                //显示密码
                ed_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                //密码显示点点
                ed_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            val password = ed_password.text.toString()
            ed_password.setSelection(password.length)
        }

        //注册
        tv_register.setOnClickListener {
            startActivity(Intent(LoginActivity@ this, RegisterActivity::class.java))
        }
        //忘记密码
        tv_forget.setOnClickListener {
            startActivity(Intent(LoginActivity@ this, ResetPasswordActivity::class.java))
        }
        //登录
        btn_submit.setOnClickListener {
            if (DYHelper.LOGIN_HELPER != null) {
                val userName = ed_phone.text.toString()
                val password = ed_password.text.toString()
                DYHelper.LOGIN_HELPER.checkUserName(userName, password)
            }
//            submitLogin()
        }
    }

    /**
     * 设置登录按钮是否可点击
     */
    private fun setSubmitButtonEnable() {
        val phone = ed_phone.text.toString()
        val password = ed_password.text.toString()
        btn_submit.isEnabled =
            (ValidatorUtils.isPassword(password) && ValidatorUtils.isMobile(phone))
    }

    /**
     * 登录
     */
    private fun submitLogin() {
        val userName = ed_phone.text.toString()
        val password = ed_password.text.toString()
        mPresenter?.login(userName, password)
    }

    override fun onLoginSuccess() {
        startActivity(Intent(LoginActivity@ this, HomeActivity::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCloseLoading(event: CloseLoadingEvent) {
        hideLoading()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCloseLoginPage(event: CloseLoginPageEvent) {
        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}