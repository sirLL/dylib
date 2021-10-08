package cn.dianyinhuoban.hm.mvp.setting.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.auth.contract.AuthStatusContract
import cn.dianyinhuoban.hm.mvp.auth.presenter.AuthStatusPresenter
import cn.dianyinhuoban.hm.mvp.auth.view.RealnameAuthActivity
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.SettingPresenter
import coil.load
import coil.transform.CircleCropTransformation
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_me.*

class AuthActivity : BaseActivity<SettingPresenter>(), SettingContract.View,
    AuthStatusContract.View {

    var mStatusPresenter: AuthStatusPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setTitle("授权书")

        tv_auth_apply.setOnClickListener {
            mPresenter.authApply()
        }

        //el_confirm.visibility = View.GONE
        //el_default.visibility = View.VISIBLE

        mStatusPresenter = AuthStatusPresenter(this)
        mStatusPresenter?.fetchAuthResult()

    }

    override fun getPresenter(): SettingPresenter? {
        return SettingPresenter(this)
    }

    override fun onApplySuccess() {
        super.onApplySuccess()
        showToast("申请成功")

    }

    override fun onApplyFail(errorMsg: String) {
        super.onApplyFail(errorMsg)
        if (errorMsg.equals("请先通过实名认证")) {
            AlertDialog
                .Builder(AuthActivity@ this)
                .setTitle("提醒")
                .setMessage("你还没有实名，是否立即实名？")
                .setPositiveButton("实名", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        RealnameAuthActivity.open(this@AuthActivity)
                    }

                }).setNegativeButton("取消", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }

                }).create().show()

        } else if ("审核中，请不要重复申请".equals(errorMsg)) {
            el_confirm.visibility = View.VISIBLE
            el_default.visibility = View.GONE
        } else {

        }
    }

    //授权书状态 1 未申请 2 审核中 3 通过 4 失败
    override fun bindAuthResult(authResult: AuthResult?) {
        when (authResult?.authorizationStatus) {
            "2" -> {
                el_confirm.visibility = View.VISIBLE
                el_default.visibility = View.GONE
                tv_ok.setOnClickListener {
                    finish()
                }
            }

            "3" -> {

                el_confirm.visibility = View.GONE
                el_default.visibility = View.GONE

                el_success.visibility = View.VISIBLE

                img_auth.load(authResult.authorizationImg)
                img_auth.load(authResult.authorizationImg) {
                    allowHardware(false)
                    crossfade(true)
                }
            }
            else -> {
                el_confirm.visibility = View.GONE
                el_default.visibility = View.VISIBLE
            }
        }

    }


}