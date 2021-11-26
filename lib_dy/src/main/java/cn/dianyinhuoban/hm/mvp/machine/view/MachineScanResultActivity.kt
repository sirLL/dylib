package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class MachineScanResultActivity : BaseActivity<IPresenter?>() {
    var mCashBackAmount: String? = null

    companion object {
        fun openScanResultActivity(
            activity: Activity,
            machineType: MachineTypeBean,
            cashBackAmount: String?,
            requestCode: Int
        ) {
            val intent = Intent(activity, MachineScanResultActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("type", machineType)
            bundle.putString("cashBackAmount", cashBackAmount)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, requestCode)
        }

        fun openScanResultActivity(
            fragment: Fragment,
            machineType: MachineTypeBean,
            cashBackAmount: String?,
            requestCode: Int
        ) {
            val intent = Intent(fragment.requireContext(), MachineScanResultActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("type", machineType)
            bundle.putString("cashBackAmount", cashBackAmount)
            intent.putExtras(bundle)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_machine_scan_result)
        setTitle("机具扫码")
        val machineType = intent.extras?.getParcelable<MachineTypeBean>("type")
        mCashBackAmount = intent?.extras?.getString("cashBackAmount", "")
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fl_container,
                MachineScanResultFragment.newInstance(machineType, mCashBackAmount),
                "MachineScanResultFragment"
            ).commit()
    }
}