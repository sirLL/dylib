package cn.dianyinhuoban.hm.mvp.auth.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthStatusContract
import cn.dianyinhuoban.hm.mvp.auth.presenter.AuthStatusPresenter
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_auth_info.*

class AuthInfoActivity : BaseActivity<AuthStatusPresenter?>(), AuthStatusContract.View {

    companion object {
        fun open(context: Context) {
            var intent = Intent(context, AuthInfoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_info)
        setTitle("实名认证")
        mPresenter?.fetchAuthResult()
    }

    override fun getPresenter(): AuthStatusPresenter? {
        return AuthStatusPresenter(this)
    }

    override fun bindAuthResult(authResult: AuthResult?) {
        tv_name.text = authResult?.auth_name
        tv_id_card.text = authResult?.auth_sn
    }
}