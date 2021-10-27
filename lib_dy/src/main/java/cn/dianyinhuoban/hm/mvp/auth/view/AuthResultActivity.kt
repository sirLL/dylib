package cn.dianyinhuoban.hm.mvp.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthStatusContract
import cn.dianyinhuoban.hm.mvp.auth.presenter.AuthStatusPresenter
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_auth_result.*

class AuthResultActivity : BaseActivity<AuthStatusPresenter?>(), AuthStatusContract.View {

    companion object {

        fun open(context: Context) {
            var intent = Intent(context, AuthResultActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_auth_result)
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.fetchAuthResult()
    }

    override fun getPresenter(): AuthStatusPresenter? {
        return AuthStatusPresenter(this)
    }

    override fun bindAuthResult(authResult: AuthResult?) {
        tv_status.text = if (authResult == null || authResult.status.isNullOrBlank()) {
            iv_status.setImageResource(R.drawable.dy_ic_order_result_wait)
            tv_status_des.text = "您尚未提交认证"
            tv_return_home.text = "去认证"
            tv_return_home.setOnClickListener {
                RealnameAuthActivity.open(AuthResultActivity@ this)
                finish()
            }
            "未认证"
        } else {
            when (authResult.status) {
                "0" -> {
                    iv_status.setImageResource(R.drawable.dy_ic_order_result_wait)
                    tv_status_des.text = "正在审核中，请耐心等待"
                    tv_return_home.text = "返回首页"
                    tv_return_home.setOnClickListener {
                        finish()
                    }
                    "审核中"
                }
                "2" -> {
                    iv_status.setImageResource(R.drawable.dy_ic_order_result_success)
                    tv_status_des.text = "恭喜您，认证成功"
                    tv_return_home.text = "返回首页"
                    tv_return_home.setOnClickListener {
                        finish()
                    }
                    "认证成功"
                }
                else -> {
                    iv_status.setImageResource(R.drawable.dy_ic_order_result_fail)
                    tv_status_des.text = "抱歉，认证失败"
                    tv_return_home.text = "重新认证"
                    tv_return_home.setOnClickListener {
                        RealnameAuthActivity.open(AuthResultActivity@ this)
                        finish()
                    }
                    "不通过"
                }
            }
        }
    }
}