package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class MachinePickerActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    companion object {
        fun openMachinePicker(
            activity: Activity,
            machineType: MachineTypeBean,
            checkedIDList: ArrayList<String>,
            requestCode: Int
        ) {
            val intent = Intent(activity, MachinePickerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("type", machineType)
            bundle.putStringArrayList("checkedIDList", checkedIDList)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, requestCode)
        }

        fun openMachinePicker(
            fragment: Fragment,
            machineType: MachineTypeBean,
            checkedIDList: ArrayList<String>,
            requestCode: Int
        ) {
            val intent = Intent(fragment.requireContext(), MachinePickerActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("type", machineType)
            bundle.putStringArrayList("checkedIDList", checkedIDList)
            intent.putExtras(bundle)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_machine_picker)
        setTitle("机具选择")
        val machineType = intent?.extras?.getParcelable<MachineTypeBean>("type")
        val checkedIDList = intent.extras?.getStringArrayList("checkedIDList")
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, MachinePickerFragment.newInstance(machineType, checkedIDList))
            .commit()
    }
}