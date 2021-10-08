package cn.dianyinhuoban.hm.mvp.order.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class AddressManagerActivity : BaseActivity<IPresenter?>() {
    companion object {
        fun openAddressManagerActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, AddressManagerActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean("isPicker", true)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, requestCode)
        }

        fun openAddressManagerActivity(context: Context) {
            val intent = Intent(context, AddressManagerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("地址列表")
        setContentView(R.layout.dy_activity_address_manager)
        val isPicker = intent?.extras?.getBoolean("isPicker", false)
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fl_container,
                AddressManagerFragment.newInstance(isPicker ?: false),
                "AddressManagerFragment"
            ).commit()
    }
}