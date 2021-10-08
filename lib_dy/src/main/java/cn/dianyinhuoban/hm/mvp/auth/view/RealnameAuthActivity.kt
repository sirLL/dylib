package cn.dianyinhuoban.hm.mvp.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthContract
import cn.dianyinhuoban.hm.mvp.auth.presenter.AuthPresenter
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import kotlinx.android.synthetic.main.activity_realname_auth.*

class RealnameAuthActivity : BaseActivity<AuthPresenter?>(), AuthContract.View {

    companion object {
        fun open(context: Context) {
            var intent = Intent(context, RealnameAuthActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realname_auth)
        setTitle("实名认证")
        setupAction()
        tv_realname_next.setOnClickListener {
            submitAuth()
        }
    }

    private fun setupAction() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                setSubmitEnable()
            }

        }
        ed_name.addTextChangedListener(textWatcher)
        ed_id_card.addTextChangedListener(textWatcher)
    }

    private fun setSubmitEnable() {
        val name = ed_name.text.toString()
        val idCard = ed_id_card.text.toString()
        tv_realname_next.isEnabled = name.length >= 2 && ValidatorUtils.isIDCard(idCard)
    }

    override fun getPresenter(): AuthPresenter? {
        return AuthPresenter(this)
    }

    private fun submitAuth() {
        val name = ed_name.text.toString()
        val idCard = ed_id_card.text.toString()
        mPresenter?.submitAuth(name, idCard)
    }

    override fun onSubmitAuthSuccess() {
        showToast("提交成功")
        finish()
    }
}