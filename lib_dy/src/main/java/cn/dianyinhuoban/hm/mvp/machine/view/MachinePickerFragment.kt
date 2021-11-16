package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineItemBean
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineQueryContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineQueryPresenter
import cn.dianyinhuoban.hm.mvp.machine.view.TransferActivity.Companion.PICKER_TYPE_MULTI
import cn.dianyinhuoban.hm.mvp.machine.view.TransferActivity.Companion.PICKER_TYPE_SELECTOR
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.widget.DividerDecoration
import kotlinx.android.synthetic.main.dy_fragment_machine_picker.*
import kotlinx.android.synthetic.main.dy_item_machine_picker.view.*

class MachinePickerFragment : BaseListFragment<MachineItemBean?, MachineQueryPresenter?>(),
    MachineQueryContract.View {
    private var mMachineType: MachineTypeBean? = null
    private val mCheckedIDList: MutableList<String> by lazy {
        mutableListOf()
    }

    companion object {
        const val RC_SCAN_MACHINE_SN = 1021
        const val RC_MACHINE_MULTI = 1022
        fun newInstance(
            machineType: MachineTypeBean?,
            checkedIDList: ArrayList<String>?
        ): MachinePickerFragment {
            val args = Bundle()
            args.putParcelable("type", machineType)
            args.putStringArrayList("checkedIDList", checkedIDList)
            val fragment = MachinePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerDecoration(
            ContextCompat.getColor(requireContext(), R.color.dy_color_divider),
            DimensionUtils.dp2px(context, 1),
            DimensionUtils.dp2px(context, 16),
            DimensionUtils.dp2px(context, 16)
        )
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_machine_picker
    }

    override fun getPresenter(): MachineQueryPresenter? {
        return MachineQueryPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMachineType = arguments?.getParcelable("type")
        val checkedIDList = arguments?.getStringArrayList("checkedIDList")
        checkedIDList?.let {
            for (id in it) {
                if (!TextUtils.isEmpty(id)) {
                    mCheckedIDList.add(id)
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingView()
        onRequest(DEF_START_PAGE)

        btn_submit.setOnClickListener {
            val checkedData: ArrayList<MachineItemBean> = getCheckedData()
            setResult(checkedData, mMachineType)
        }

        fl_tab_scan.setOnClickListener {
            mMachineType?.let {
                MachineScanResultActivity.openScanResultActivity(this, it, RC_SCAN_MACHINE_SN)
            }
        }

        fl_tab_multi.setOnClickListener {
            TransferMultiActivity.openTransferMultiActivity(this, mMachineType, RC_MACHINE_MULTI)
        }
    }

    private fun setResult(checkedData: ArrayList<MachineItemBean>?, machineType: MachineTypeBean?) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("type", machineType)
        bundle.putParcelableArrayList("checkedData", checkedData)
        bundle.putString("pickerType", PICKER_TYPE_SELECTOR)
        intent.putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun getCheckedData(): ArrayList<MachineItemBean> {
        val checkedData = arrayListOf<MachineItemBean>()
        val data = mAdapter?.data
        data?.let {
            for (item in it) {
                item?.let { item_ ->
                    for (id in mCheckedIDList) {
                        if (id == item_.id) {
                            checkedData.add(item_)
                        }
                    }
                }
            }
        }
        return checkedData
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchMachine(mMachineType?.id ?: "", "1", "", page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_machine_picker
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
        viewHolder?.itemView?.iv_check_box?.isSelected = if (itemData == null) {
            false
        } else {
            mCheckedIDList.contains(itemData?.id)
        }
    }

    override fun onItemClick(data: MachineItemBean?, position: Int) {
        data?.let {
            if (mCheckedIDList.contains(it.id)) {
                mCheckedIDList.remove(it.id)
            } else {
                mCheckedIDList.add(it.id)
            }
            mAdapter?.notifyItemChanged(position)
        }
        setSubmitEnable()
    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {

    }

    override fun bindMachine(data: MyMachineBean?) {
        loadData(data?.data)
    }

    override fun bindMachineBySN(data: MyMachineBean?, requestCode: Int) {

    }

    private fun setSubmitEnable() {
        btn_submit.isEnabled = mCheckedIDList.size > 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_SCAN_MACHINE_SN -> {
                    val checkedData =
                        data?.extras?.getParcelableArrayList<MachineItemBean>("checkedData")
                    val machineType = data?.extras?.getParcelable<MachineTypeBean>("type")
                    setResult(checkedData, machineType)
                }
                RC_MACHINE_MULTI -> {
                    val startSN = data?.extras?.getString("startSN")
                    val endSN = data?.extras?.getString("endSN")
                    val machineType = data?.extras?.getParcelable<MachineTypeBean>("type")
                    val machineCount = data?.extras?.getInt("machineCount", 0)
                    val intent = Intent()
                    val bundle = Bundle()
                    bundle.putParcelable("type", machineType)
                    bundle.putString("startSN", startSN)
                    bundle.putString("endSN", endSN)
                    bundle.putString("pickerType", PICKER_TYPE_MULTI)
                    bundle.putInt("machineCount", machineCount ?: 0)
                    intent.putExtras(bundle)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
            }
        }
    }
}