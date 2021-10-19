package cn.dianyinhuoban.hm.mvp.income.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.*
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.contract.WithdrawContract
import cn.dianyinhuoban.hm.mvp.income.presenter.BankCardListPresenter
import cn.dianyinhuoban.hm.mvp.income.presenter.WithdrawPresenter
import cn.dianyinhuoban.hm.util.StringUtil
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import cn.dianyinhuoban.hm.widget.dialog.PayPwdDialog
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.filter.NumberFilter
import kotlinx.android.synthetic.main.dy_activity_withdraw.*

class WithdrawActivity : BaseActivity<WithdrawPresenter?>(), WithdrawContract.View {
    private var mCheckedBankCard: BankBean? = null
    private var mBalance: Double = 0.0
    private var mWithdrawTypePicker: WithdrawTypePicker? = null

    override fun getToolbarColor(): Int {
        return ContextCompat.getColor(WithdrawActivity@ this, R.color.dy_base_color_page_bg)
    }

    override fun getStatusBarColor(): Int {
        return R.color.dy_base_color_page_bg
    }

    override fun getPresenter(): WithdrawPresenter? {
        return WithdrawPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_withdraw)
        setTitle("提现")
        ed_amount.filters = arrayOf<InputFilter>(NumberFilter())
        setRightButtonText("提现明细") {
            startActivity(Intent(WithdrawActivity@ this, WithdrawRecordActivity::class.java))
        }
        cl_type_container.setOnClickListener {
            showWithdrawTypePicker()
        }
        tv_all.setOnClickListener {
            val balance = NumberUtils.numberScale(mBalance)
            ed_amount.setText(balance)
            ed_amount.setSelection(balance.length)
        }
        btn_submit.setOnClickListener {
            PayPwdDialog(WithdrawActivity@ this)
                .setNumRand(true)
                .setInputComplete(object : PayPwdDialog.OnInputCodeListener {
                    override fun inputCodeComplete(dialog: Dialog?, verificationCode: String?) {
                        dialog?.dismiss()
                        submitWithdraw(verificationCode ?: "")
                    }

                    override fun inputCodeInput(dialog: Dialog?, verificationCode: String?) {
                    }
                })
                .show()
        }
        setupEditText()
    }

    override fun onStart() {
        super.onStart()
        fetchBalance()
        fetchBankCard()
        fetchWithdrawFee("")
    }

    private fun showWithdrawTypePicker() {
        if (mWithdrawTypePicker == null) {
            mWithdrawTypePicker = WithdrawTypePicker.newInstance()
            mWithdrawTypePicker?.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<BankBean, BankCardListPresenter> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<BankBean, BankCardListPresenter>,
                    t: BankBean?,
                    position: Int
                ) {
                    bindCheckedBankCard(t)
                }
            })
        }
        mWithdrawTypePicker?.setCheckedID(mCheckedBankCard?.id)
        mWithdrawTypePicker?.show(supportFragmentManager, "WithdrawTypePicker")
    }

    private fun bindCheckedBankCard(bankBean: BankBean?) {
        if (bankBean != null) {
            tv_type.text = "${bankBean?.bankName}(${StringUtil.getBankCardEndNo(bankBean?.bankNo)})"
        } else {
            tv_type.text = ""
        }
        mCheckedBankCard = bankBean
        setSubmitButtonEnable()
    }

    private fun setupEditText() {
        ed_amount.filters = arrayOf(NumberFilter())
        ed_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setSubmitButtonEnable()
            }
        })
    }

    private fun setSubmitButtonEnable() {
        val amount = ed_amount.text.toString()
        var amountDouble = 0.0
        if (amount.isNotEmpty()) {
            amountDouble = amount.toDouble()
        }
        btn_submit.isEnabled = (amountDouble > 0 && amountDouble <= mBalance)
    }

    /************************************个人余额  START***********************************/
    private fun fetchBalance() {
        mPresenter?.fetchPersonalData()
    }

    override fun bindPersonalData(personalBean: PersonalBean?) {
        mBalance = if (TextUtils.isEmpty(personalBean?.total)) {
            0.0
        } else {
            NumberUtils.numberScale(personalBean?.total).toDouble()
        }
        tv_balance.text = NumberUtils.numberScale(mBalance)
        setSubmitButtonEnable()
    }
    /************************************个人余额  END***********************************/

    /************************************提现手续费  START***********************************/
    private fun fetchWithdrawFee(amount: String) {
        mPresenter?.getWithdrawFee(amount)
    }

    override fun bindWithdrawFee(fee: String?) {
        var content = ""
        fee?.let {
            content = it.replace("\\n", "\n")
        }
        tv_tip.text = content
    }
    /************************************提现手续费  END*************************************/


    /************************************银行卡  START*************************************/
    private fun fetchBankCard() {
        mPresenter?.getBankList()
    }

    override fun onLoadBankList(bankBeanList: List<BankBean>) {
        if (mCheckedBankCard == null) {
            bankBeanList?.let {
                if (bankBeanList?.isNotEmpty()) {
                    bindCheckedBankCard(bankBeanList[0])
                }
            }
        }
    }
    /************************************银行卡  END*************************************/

    /************************************发起提现  START*************************************/
    private fun submitWithdraw(password: String) {
        mPresenter?.submitWithdraw(
            mCheckedBankCard?.id ?: "",
            NumberUtils.numberScale(ed_amount.text.toString()),
            password
        )
    }

    override fun onSubmitWithdrawSuccess() {
        showToast("发起提现成功")
//        startActivity(Intent(WithdrawActivity@ this, WithdrawSuccessActivity::class.java))
        finish()
    }

    /************************************发起提现  END*************************************/

}