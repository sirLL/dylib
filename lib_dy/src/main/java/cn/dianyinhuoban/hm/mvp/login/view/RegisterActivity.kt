package cn.dianyinhuoban.hm.mvp.login.view

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.CountdownTextUtils
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.api.URLConfig
import cn.dianyinhuoban.hm.mvp.WebActivity
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.login.contract.RegisterContract
import cn.dianyinhuoban.hm.mvp.login.presenter.RegisterPresenter
import cn.dianyinhuoban.hm.widget.dialog.ImageCodeDialog
import com.wareroom.lib_base.api.BaseURLConfig
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.OSUtils
import com.wareroom.lib_base.utils.ValidatorUtils
import kotlinx.android.synthetic.main.dy_activity_login.*
import kotlinx.android.synthetic.main.dy_activity_register.*
import kotlinx.android.synthetic.main.dy_activity_register.btn_submit
import kotlinx.android.synthetic.main.dy_activity_register.ed_password
import kotlinx.android.synthetic.main.dy_activity_register.ed_phone
import kotlinx.android.synthetic.main.dy_activity_register.iv_eye

class RegisterActivity : BaseActivity<RegisterPresenter?>(), RegisterContract.View {

    private val mImageCodeDialog: ImageCodeDialog by lazy {
        ImageCodeDialog.newInstance()
    }

    override fun getPresenter(): RegisterPresenter? {
        return RegisterPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_register)
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
        ed_invite_code.addTextChangedListener(textWatcher)
        setupAgreement()
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

        iv_pay_eye.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                //显示密码
                ed_pay_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                //密码显示点点
                ed_pay_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            val password = ed_pay_password.text.toString()
            ed_pay_password.setSelection(password.length)
        }

        tv_send.setOnClickListener {
            fetchImageCode()
        }
        btn_submit.setOnClickListener {
            submitRegister()
        }
    }

    private fun setSubmitButtonEnable() {
        val phone = ed_phone.text.toString()
        val smsCode = ed_sms_code.text.toString()
        val password = ed_password.text.toString()
        val payPassword = ed_pay_password.text.toString()
        val inviteCode = ed_invite_code.text.toString()
        btn_submit.isEnabled = (
                ValidatorUtils.isMobile(phone)
                        && ValidatorUtils.isPassword(password)
                        && ValidatorUtils.isPayPassword(payPassword)
                        && !TextUtils.isEmpty(smsCode)
                        && !TextUtils.isEmpty(inviteCode))
    }

    private fun setupAgreement() {
        val agreementText = "点击注册，即表示您同意《电银管家泓盟版用户协议》"
        val spannableStringBuilder = SpannableStringBuilder(agreementText)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebActivity.openWebActivity(
                    this@RegisterActivity,
                    "用户协议",
                    BaseURLConfig.BASE_URL + URLConfig.PAGE_USER_AGREEMENT
                )
            }
        }
        val colorSpan = ForegroundColorSpan(
            ContextCompat.getColor(
                RegisterActivity@ this,
                R.color.color_1b68eb
            )
        )
        spannableStringBuilder.setSpan(clickableSpan, 11, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringBuilder.setSpan(colorSpan, 11, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_agreement.text = spannableStringBuilder
        tv_agreement.highlightColor = Color.TRANSPARENT;
        tv_agreement.movementMethod = LinkMovementMethod.getInstance();
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

    private fun submitRegister() {
        val userName = ed_phone.text.toString()
        val code = ed_sms_code.text.toString()
        val password = ed_password.text.toString()
        val payPassword = ed_pay_password.text.toString()
        val inviteCode = ed_invite_code.text.toString()
        mPresenter?.register(
            userName,
            code,
            password,
            payPassword,
            inviteCode,
            OSUtils.getDeviceId(applicationContext)
        )
    }

    override fun onRegisterSuccess() {
        showToast("注册成功")
        finish()
    }


}