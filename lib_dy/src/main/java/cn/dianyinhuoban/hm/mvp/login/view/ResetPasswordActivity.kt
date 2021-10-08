package cn.dianyinhuoban.hm.mvp.login.view

import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import cn.dianyinhuoban.hm.CountdownTextUtils
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.login.contract.RegisterContract
import cn.dianyinhuoban.hm.mvp.login.presenter.RegisterPresenter
import cn.dianyinhuoban.hm.widget.dialog.ImageCodeDialog
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.btn_submit
import kotlinx.android.synthetic.main.activity_reset_password.ed_password
import kotlinx.android.synthetic.main.activity_reset_password.ed_phone
import kotlinx.android.synthetic.main.activity_reset_password.ed_sms_code
import kotlinx.android.synthetic.main.activity_reset_password.iv_eye
import kotlinx.android.synthetic.main.activity_reset_password.tv_send

class ResetPasswordActivity : BaseActivity<RegisterPresenter?>(), RegisterContract.View {

    private val mImageCodeDialog: ImageCodeDialog by lazy {
        ImageCodeDialog.newInstance()
    }

    override fun getPresenter(): RegisterPresenter? {
        return RegisterPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        setTitle("找回密码")
        initView()
    }

    private fun initView() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setSubmitButtonEnable()
            }
        }
        ed_phone.addTextChangedListener(textWatcher)
        ed_sms_code.addTextChangedListener(textWatcher)
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

        btn_submit.setOnClickListener {
            var phone = ed_phone.text.toString().trim()
            var code = ed_sms_code.text.toString().trim()
            val password = ed_password.text.toString().trim()
            if (phone.isEmpty()) {
                showToast("请输入手机号码")
                return@setOnClickListener
            }

            if (code.isEmpty()) {
                showToast("请输入验证码")
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                showToast("请输入密码")
                return@setOnClickListener
            }

            mPresenter?.forgetPassword(phone,code,password)

        }


        tv_send.setOnClickListener {
            fetchImageCode()
        }
    }

    private fun setSubmitButtonEnable() {
        val phone = ed_phone.text.toString()
        val smsCode = ed_sms_code.text.toString()
        val password = ed_password.text.toString()
        btn_submit.isEnabled = (
                ValidatorUtils.isMobile(phone)
                        && ValidatorUtils.isPassword(password)
                        && !TextUtils.isEmpty(smsCode))
    }

    override fun onForgetSuccess() {
        super.onForgetSuccess()
        showToast("找回密码成功")
        finish()
    }

    override fun showImageCode(imageCode: ImageCodeBean?) {
        imageCode?.let {
            if (!mImageCodeDialog.isAdded) {
                mImageCodeDialog.show(supportFragmentManager, "ImageCodeDialog")
                mImageCodeDialog.setImageCodeCallBack(object : ImageCodeDialog.ImageCodeCallBack {
                    override fun getImageCode(code: String?, inputCode: String) {
                        if (code != null) {
                            sendSMS(inputCode, code)
                        }
                    }

                    override fun changeImage() {
                        fetchImageCode()
                    }
                })
            }
            mImageCodeDialog.loadImageCode(it.code, it.img)
        }
    }

    private fun sendSMS(imageCode: String, imageKey: String) {
        val phone = ed_phone.text.toString()
        if (phone.isEmpty()) {
            showToast("输入您的手机号")
            return
        }
        if (!ValidatorUtils.isMobile(phone)) {
            showToast("请输入正确的手机号")
            return
        }
        mPresenter?.let {
            it.onSendSMS(phone, imageKey, imageCode)
        }
    }

    override fun startSMSCountdown() {
        CountdownTextUtils(this).startCountdown(tv_send, "发送验证码")
    }

    private fun fetchImageCode() {
        val phone = ed_phone.text.toString()
        if (phone.isEmpty()) {
            showToast("输入您的手机号")
            return
        }
        if (!ValidatorUtils.isMobile(phone)) {
            showToast("请输入正确的手机号")
            return
        }

        mPresenter?.fetchImageCode()
    }


}