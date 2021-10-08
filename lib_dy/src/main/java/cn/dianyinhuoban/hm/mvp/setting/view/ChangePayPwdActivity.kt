package cn.dianyinhuoban.hm.mvp.setting.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.setting.contract.PayPasswordContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.PayPasswordPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_change_password.*
import kotlinx.android.synthetic.main.dy_activity_register.*

class ChangePayPwdActivity : BaseActivity<PayPasswordPresenter>(), PayPasswordContract.View {

    companion object {
        fun open(context: Activity) {
            var intent = Intent(context, ChangePayPwdActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_change_pay_password)
        setTitle("修改支付密码")

        btn_change_pwd_submit.setOnClickListener {
            val newPassword = ed_change_new_password.text.toString().trim()
            val oldPassword = ed_change_password.text.toString().trim()
            val confirmPassword = ed_change_confirm_password.text.toString().trim()
            if (oldPassword.isEmpty()) {
                showToast("请输入原密码")
                return@setOnClickListener
            }

            if (newPassword.isEmpty()) {
                showToast("请输入新密码")
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                showToast("请输入确认密码")
                return@setOnClickListener
            }

            mPresenter.submitPayPassword(oldPassword, newPassword, confirmPassword)

        }


        iv_find_old_eye.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                //显示密码
                ed_change_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                //密码显示点点
                ed_change_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            val password = ed_change_password.text.toString()
            ed_change_password.setSelection(password.length)
        }

        iv_find_new_eye.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                ed_change_new_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                ed_change_new_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }

            var password = ed_change_new_password.text.toString()
            ed_change_new_password.setSelection(password.length)
        }

        iv_find_confirm_eye.setOnClickListener {
            it.isSelected = !it.isSelected

            if (it.isSelected) {
                ed_change_confirm_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                ed_change_confirm_password.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }

            var password = ed_change_confirm_password.text.toString()
            ed_change_confirm_password.setSelection(password.length)
        }

    }

    override fun getPresenter(): PayPasswordPresenter {
        return PayPasswordPresenter(this)
    }

    override fun onSubmitPayPasswordSuccess() {
        showToast("密码已经修改成功")
        finish()
    }

}