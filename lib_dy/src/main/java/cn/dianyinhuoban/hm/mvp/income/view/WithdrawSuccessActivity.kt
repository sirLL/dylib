package cn.dianyinhuoban.hm.mvp.income.view


import android.content.Context
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_withdraw_success.*

class WithdrawSuccessActivity : BaseActivity<IPresenter?>() {
    companion object {
        fun openWithdrawSuccessActivity(context: Context, bankBean: BankBean, amount: String) {

        }
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw_success)

        btn_submit.setOnClickListener {
            finish()
        }
    }


}