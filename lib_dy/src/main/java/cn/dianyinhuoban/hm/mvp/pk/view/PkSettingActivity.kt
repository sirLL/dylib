package cn.dianyinhuoban.hm.mvp.pk.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.event.LaunchPkSuccessEvent
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import cn.dianyinhuoban.hm.mvp.pk.contract.PKContract
import cn.dianyinhuoban.hm.mvp.pk.presenter.PKPresenter
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_pk_setting.*
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal

class PkSettingActivity : BaseActivity<PKPresenter>(), PKContract.View {

    var mPkMember: PkMember? = null
    var mType: String? = null
    var mSelectCycle: String = "1"

    companion object {
        const val PK_TYPE_PERSONAL = "1"
        const val PK_TYPE_TEAM = "2"

        fun open(context: Context, data: PkMember, type: String) {
            var intent = Intent(context, PkSettingActivity::class.java)
            intent.putExtra("data", data)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pk_setting)

        mPkMember = intent.getParcelableExtra("data")
        mType = intent.getStringExtra("type")

        if (mPkMember == null || mType == null) {
            showToast("参数异常")
            finish()
            return
        }

        Log.d("PK", "PkMember:" + mPkMember?.name + ", Type: " + mType)

        setTitle(if (mType.equals(PK_TYPE_PERSONAL)) "个人PK设置" else "团队PK设置")

        setupListener()

    }

    private fun setupListener() {
        el_pk_period.setOnClickListener {
            val items = arrayOf("一天", "七天", "三十天")
            val items2 = arrayOf("1", "7", "30")

            AlertDialog
                .Builder(PkSettingActivity@ this)
                .setItems(items) { _, which ->
                    val item = items[which]
                    tv_pk_period.text = item
                    mSelectCycle = items2[which]
                }.create().show()
        }

        tv_pk.setOnClickListener {
            var pkAmount = ed_pk_amount.text.toString().trim()
            if (pkAmount.isEmpty()) {
                showToast("请输入PK奖励金额")
                return@setOnClickListener
            }

            if (BigDecimal(pkAmount).compareTo(BigDecimal.ZERO) <= 0) {
                showToast("请输入正确的奖励金额")
                return@setOnClickListener
            }

            mPresenter.launchPk(mType!!, mPkMember?.uid!!, mSelectCycle, pkAmount)

        }
    }

    override fun getPresenter(): PKPresenter {
        return PKPresenter(this)
    }

    override fun onLaunchPkSuccess() {
        super.onLaunchPkSuccess()
        EventBus.getDefault().post(LaunchPkSuccessEvent())
        showToast("发起PK成功")
        finish()
//        PkResultActivity.open(this)
    }

}