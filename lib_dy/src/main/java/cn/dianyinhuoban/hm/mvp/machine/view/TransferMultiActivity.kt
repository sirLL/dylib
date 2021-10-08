package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TransferMultiActivity : BaseActivity<IPresenter?>() {

    var mMachineType: MachineTypeBean? = null

    companion object {
        fun openTransferMultiActivity(
            activity: Activity,
            machineType: MachineTypeBean?,
            requestCode: Int
        ) {
            val intent = Intent(activity, TransferMultiActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("type", machineType)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, requestCode)
        }

        fun openTransferMultiActivity(
            fragment: Fragment,
            machineType: MachineTypeBean?,
            requestCode: Int
        ) {
            val intent = Intent(fragment.context, TransferMultiActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("type", machineType)
            intent.putExtras(bundle)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_transfer_multi)
        setTitle("批量划分")
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, TransferMultiFragment.newInstance(mMachineType)).commit()
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mMachineType = bundle?.getParcelable("type")
    }
}