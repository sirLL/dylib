package cn.dianyinhuoban.hm.mvp.me.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.CountdownTextUtils
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.hm.mvp.setting.contract.BankContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.BankPresenter
import cn.dianyinhuoban.hm.widget.dialog.ImageCodeDialog
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import kotlinx.android.synthetic.main.dy_activity_bind_bank_card.*
import kotlinx.android.synthetic.main.dy_activity_bind_bank_card.btn_submit
import kotlinx.android.synthetic.main.dy_activity_bind_bank_card.ed_phone
import kotlinx.android.synthetic.main.dy_activity_register.*

class BindBankCardActivity : BaseActivity<BankPresenter?>(), BankContract.View {

    companion object {
        fun open(context: Context) {
            var intent = Intent(context, BindBankCardActivity::class.java)
            context.startActivity(intent)
        }

        fun open(context: Activity, requestCode: Int) {
            var intent = Intent(context, BindBankCardActivity::class.java)
            context.startActivityForResult(intent, requestCode)
        }

        fun open(context: Activity, bankCard: BankBean, requestCode: Int) {
            val intent = Intent(context, BindBankCardActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("bankCard", bankCard)
            intent.putExtras(bundle)
            context.startActivityForResult(intent, requestCode)
        }
    }

    private var mBankCard: BankBean? = null

    private val mImageCodeDialog: ImageCodeDialog by lazy {
        ImageCodeDialog.newInstance()
    }

    override fun getPresenter(): BankPresenter? {
        return BankPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mBankCard == null) {
            setTitle("绑定银行卡")
        } else {
            setTitle("修改银行卡")
        }
        setContentView(R.layout.dy_activity_bind_bank_card)
        bindBankCard()
        btn_submit.setOnClickListener {
            val bankUsername = ed_name.text.toString().trim()
            val bankName = ed_bank.text.toString().trim();
            val bankNo = ed_card_no.text.toString().trim();
            val phoneNumber = ed_phone.text.toString().trim();
            val phoneCode = ed_code.text.toString().trim();

            if (bankUsername.isEmpty()) {
                showToast("请输入姓名")
                return@setOnClickListener
            }

            if (bankName.isEmpty()) {
                showToast("请输入开户行")
                return@setOnClickListener
            }

            if (bankNo.isEmpty()) {
                showToast("请输入银行卡号")
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                showToast("请输入您的银行预留手机号")
                return@setOnClickListener
            }
//
//            if (phoneCode.isEmpty()) {
//                showToast("请输入验证码")
//                return@setOnClickListener
//            }

            if (mBankCard == null || mBankCard?.id.isNullOrBlank()) {
                mPresenter?.addBank(bankUsername, bankName, bankNo, phoneNumber, phoneCode)
            } else {
                mPresenter?.updateBank(
                    bankUsername,
                    bankName,
                    bankNo,
                    phoneNumber,
                    phoneCode,
                    mBankCard?.id ?: ""
                )
            }

        }

        tv_get_code.setOnClickListener {
            fetchImageCode()
        }

    }

    private fun bindBankCard() {
        mBankCard?.let {
            val bankName = it.bankName
            val bankCardNo = it.bankNo
            val name = it.name
            bankName?.let {
                ed_bank.setText(bankName)
                ed_bank.setSelection(bankName.length)
            }
            bankCardNo?.let {
                ed_card_no.setText(bankCardNo)
                ed_card_no.setSelection(bankCardNo.length)
            }
            name?.let {
                ed_name.setText(name)
                ed_name.setSelection(name.length)
            }
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mBankCard = bundle?.getParcelable("bankCard")
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
        CountdownTextUtils(this).startCountdown(tv_get_code, "发送验证码")
    }

    override fun onUpdateBankSuccess() {
        super.onUpdateBankSuccess()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onAddBankSuccess() {
        super.onAddBankSuccess()
        setResult(Activity.RESULT_OK)
        finish()
    }
}