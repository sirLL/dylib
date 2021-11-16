package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.ScanActivity
import cn.dianyinhuoban.hm.mvp.bean.MachineItemBean
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineQueryContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineQueryPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.dy_fragment_machine_scan_result.*
import kotlinx.android.synthetic.main.dy_item_transfer.view.*

class MachineScanResultFragment : BaseListFragment<MachineItemBean, MachineQueryPresenter?>(),
    MachineQueryContract.View {

    private var mMachineType: MachineTypeBean? = null

    companion object {
        const val RC_SCAN_MACHINE_SN = 1020
        fun newInstance(machineType: MachineTypeBean?): MachineScanResultFragment {
            val args = Bundle()
            args.putParcelable("type", machineType)
            val fragment = MachineScanResultFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): MachineQueryPresenter? {
        return MachineQueryPresenter(this)
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun isSupportRefresh(): Boolean {
        return false
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_machine_scan_result
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_transfer
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMachineType = arguments?.getParcelable("type")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            val checkedData: ArrayList<MachineItemBean> = getCheckedData()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelableArrayList("checkedData", checkedData)
            bundle.putParcelable("type", mMachineType)
            intent.putExtras(bundle)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

        btn_scan.setOnClickListener {
            ScanActivity.openScan(this, RC_SCAN_MACHINE_SN)
        }
    }

    private fun getCheckedData(): ArrayList<MachineItemBean> {
        val checkedData = arrayListOf<MachineItemBean>()
        val data = mAdapter?.data
        data?.let {
            for (item in it) {
                checkedData.add(item)
            }
        }
        return checkedData
    }

    override fun onRequest(page: Int) {

    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MachineItemBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = "${itemData?.name} ${itemData?.pos_sn}"
        viewHolder?.itemView?.tv_status?.text = if ("1" == itemData?.isNew) {
            "新机器"
        } else {
            "返还机"
        }
        viewHolder?.itemView?.iv_delete?.setOnClickListener {
            mAdapter?.data?.removeAt(position)
            mAdapter?.notifyItemRemoved(position)
            setSubmitEnable()
        }
    }

    override fun onItemClick(data: MachineItemBean?, position: Int) {

    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {

    }

    override fun bindMachine(data: MyMachineBean?) {

    }

    override fun bindMachineBySN(data: MyMachineBean?, requestCode: Int) {
        if (data?.data == null || data?.data?.isEmpty()) {
            showToast("编码无效")
            return
        } else if ("1" != data.data[0].act_status) {
            showToast("该机具已激活")
            return
        } else {
            mAdapter?.appendData(data?.data)
        }
        setSubmitEnable()
    }

    private fun setSubmitEnable() {
        btn_submit.isEnabled = mAdapter?.data != null && mAdapter?.data?.size!! > 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_SCAN_MACHINE_SN -> {
                    val scanSN = ScanActivity.getScanResult(data)
                    mPresenter?.fetchMachineBySN(mMachineType?.id ?: "", scanSN, 1, 1)
                }
            }
        }
    }
}