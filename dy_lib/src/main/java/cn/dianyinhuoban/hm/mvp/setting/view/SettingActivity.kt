package cn.dianyinhuoban.hm.mvp.setting.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.api.URLConfig
import cn.dianyinhuoban.hm.mvp.WebActivity
import cn.dianyinhuoban.hm.mvp.auth.view.AuthInfoActivity
import cn.dianyinhuoban.hm.mvp.auth.view.AuthResultActivity
import cn.dianyinhuoban.hm.mvp.auth.view.RealnameAuthActivity
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.SettingPresenter
import com.wareroom.lib_base.api.BaseURLConfig
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.AppManager
import com.wareroom.lib_base.utils.CacheFileUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity<SettingPresenter?>(), SettingContract.View {

    companion object {
        const val REQ_CODE_TEAM = 100;
    }

    private var mAuthResult: AuthResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setTitle("设置")


        tv_team_name.text = MMKVUtil.getTeam()

        el_user_profile_item.setOnClickListener {
            UserProfileActivity.open(SettingActivity@ this)
        }

        el_change_password_item.setOnClickListener {
            ChangePasswordActivity.open(SettingActivity@ this)
        }

        el_change_pay_password_item.setOnClickListener {
            ChangePayPwdActivity.open(SettingActivity@ this)
        }
        el_reset_pay_password_item.setOnClickListener {
            ResetPayPasswordActivity.open(SettingActivity@ this)
        }
        el_team_name_item.setOnClickListener {
            if (MMKVUtil.getIsTeamLeader()) {
                EditInfoActivity.open(
                    SettingActivity@ this,
                    REQ_CODE_TEAM,
                    EditInfoActivity.REQ_TYPE_TEAM,
                    MMKVUtil.getTeam()
                )
            }
        }

        el_auth_name_item.setOnClickListener {
            if (mAuthResult == null) {
                RealnameAuthActivity.open(SettingActivity@ this)
            } else {
                when (mAuthResult?.status) {
                    "0" -> {
                        AuthResultActivity.open(this)
                    }
                    "1" -> {
                        RealnameAuthActivity.open(SettingActivity@ this)
                    }
                    else -> {
                        AuthInfoActivity.open(this)
                    }
                }
            }

        }

        el_about_item.setOnClickListener {
            WebActivity.openWebActivity(
                this,
                "关于我们",
                BaseURLConfig.BASE_URL + URLConfig.PAGE_ABOUT_US
            )
        }

        el_agreement_item.setOnClickListener {
            WebActivity.openWebActivity(
                this,
                "用户协议",
                BaseURLConfig.BASE_URL + URLConfig.PAGE_USER_AGREEMENT
            )
        }

        el_clean_item.setOnClickListener {
            CacheFileUtils.clearAllCache(this)
            tv_cache_size.text = CacheFileUtils.getTotalCacheSize(this)
        }

        btn_exit.setOnClickListener {


            AlertDialog
                .Builder(SettingActivity@ this)
                .setTitle("提醒")
                .setMessage("确认退出吗？")
                .setNegativeButton(
                    "取消"
                ) { dialog, which -> }
                .setPositiveButton(
                    "退出"
                ) { dialog, which -> mPresenter?.exit() }
                .create().show()

        }

        tv_cache_size.text = CacheFileUtils.getTotalCacheSize(this)

    }

    override fun onStart() {
        super.onStart()
        fetchAuthResult()
    }

    override fun getPresenter(): SettingPresenter? {
        return SettingPresenter(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TEAM && resultCode == Activity.RESULT_OK) {
            tv_team_name.text = data?.getStringExtra("data")
            MMKVUtil.saveTeam(data?.getStringExtra("data"))
        }
    }

    private fun fetchAuthResult() {
        mPresenter?.fetchAuthResult()
    }

    override fun bindAuthResult(authResult: AuthResult?) {
        mAuthResult = authResult
        tv_realname_status.text = if (authResult == null) {
            tv_realname_status.setTextColor(ContextCompat.getColor(this, R.color.color_f60e36))
            "未认证"
        } else {
            when (authResult.status) {
                "0" -> {
                    tv_realname_status.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_f60e36
                        )
                    )
                    "审核中"
                }
                "1" -> {
                    tv_realname_status.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_f60e36
                        )
                    )
                    "不通过"
                }
                else -> {
                    tv_realname_status.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_9ea9b6
                        )
                    )
                    "已认证"
                }
            }
        }
    }

    override fun onExit() {
        super.onExit()
        AppManager.getInstance().loginOut(this)
    }


}