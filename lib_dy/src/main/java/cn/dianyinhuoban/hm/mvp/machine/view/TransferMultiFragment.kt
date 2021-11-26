package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.ScanActivity
import cn.dianyinhuoban.hm.mvp.bean.MachineItemBean
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineQueryContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineQueryPresenter
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineTypePresenter
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_fragment_transfer_multi.*
import kotlinx.android.synthetic.main.dy_item_transfer_multi.view.*

class TransferMultiFragment : BaseListFragment<MachineItemBean?, MachineQueryPresenter?>(),
    MachineQueryContract.View {
    private var mMachineType: MachineTypeBean? = null
    private var mMachineCount = 0
    private var mCashBackAmount: String? = null

    companion object {
        private const val RC_SCAN_START_MACHINE = 1
        private const val RC_SCAN_END_MACHINE = 2

        fun newInstance(machineType: MachineTypeBean?, cashBackAmount: String?): TransferMultiFragment {
            val args = Bundle()
            args.putParcelable("type", machineType)
            args.putString("cashBackAmount", cashBackAmount)
            val fragment = TransferMultiFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mMachineTypePicker: MachineTypePicker by lazy {
        MachineTypePicker.newInstance("-1")
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_transfer_multi
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
        mCashBackAmount = arguments?.getString("cashBackAmount", "0.0")

        mPresenter?.fetchMachineType()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEmpty("您还没有选中机具")
        iv_start_scan.setOnClickListener {
            ScanActivity.openScan(TransferMultiFragment@ this, RC_SCAN_START_MACHINE)
        }

        iv_end_scan.setOnClickListener {
            ScanActivity.openScan(TransferMultiFragment@ this, RC_SCAN_END_MACHINE)
        }

        //选择机具样式
        tv_machine.setOnClickListener {
            showTypePicker()
        }

        btn_submit.setOnClickListener {
//            val intent = Intent()
//            val bundle = Bundle()
//            bundle.putParcelable("type", mMachineType)
//            bundle.putString("startSN", ed_start_no.text.toString())
//            bundle.putString("endSN", ed_end_no.text.toString())
//            bundle.putInt("machineCount", mMachineCount)
//            intent.putExtras(bundle)
//            activity?.let {
//                if (!it.isDestroyed) {
//                    it.setResult(Activity.RESULT_OK, intent)
//                    it.finish()
//                }
//            }
            setResult()
        }

        ed_start_no.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val endSN = ed_end_no.text.toString()
                    if (!TextUtils.isEmpty(endSN)) {
                        ed_end_no.setText("")
                    }
                }
            }
        )
        ed_end_no.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val startSN = ed_start_no.text.toString()
                    if (!TextUtils.isEmpty(startSN)) {
                        onRefresh(mRefreshLayout)
                    }
                }
            }
        )

        mMachineType?.let {
            tv_machine.text = it.name
        }
    }

    private fun setResult() {
        val checkedData = ArrayList<MachineItemBean>()
        val data = mAdapter?.data
        val cashBackAmount = if (!mCashBackAmount.isNullOrBlank()) {
            mCashBackAmount!!.toDouble()
        } else {
            0.0
        }
        data?.let {
            for (bean in it) {
                bean?.let { itemBean ->
                    val backMoney = if (bean?.backMoney.isNullOrBlank()) {
                        0.0
                    } else {
                        NumberUtils.numberScale(bean?.backMoney).toDouble()
                    }
                    if (cashBackAmount <= backMoney) {
                        checkedData.add(itemBean)
                    }
                }
            }
        }

        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList("checkedData", checkedData)
        bundle.putParcelable("type", mMachineType)
        intent.putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun showTypePicker() {
        mMachineTypePicker.setOnItemSelectedListener(object :
            BaseBottomPicker.OnItemSelectedListener<MachineTypeBean?, MachineTypePresenter?> {
            override fun onItemSelected(
                bottomPicker: BaseBottomPicker<MachineTypeBean?, MachineTypePresenter?>,
                t: MachineTypeBean?,
                position: Int
            ) {
                bindCheckedMachineType(t)
                bottomPicker.dismiss()
            }
        })
        mMachineTypePicker.setCheckedID(mMachineType?.id)
        mMachineTypePicker.show(childFragmentManager, "MachineTypePicker")
    }

    private fun bindCheckedMachineType(machineType: MachineTypeBean?) {
        machineType?.let {
            mMachineType = it
            tv_machine.text = it.name
        }
        setSubmitEnable()
        mRefreshLayout?.autoRefresh()
    }

    private fun setSubmitEnable() {
//        btn_submit.isEnabled =
//                mMachineType != null && mAdapter?.data != null && mAdapter?.data?.isNotEmpty()!!
        val enableData = mutableListOf<MachineItemBean>()
        val cashBackAmount = if (!mCashBackAmount.isNullOrBlank()) {
            mCashBackAmount!!.toDouble()
        } else {
            0.0
        }
        mAdapter?.data?.let {
            for (bean in it) {
                bean?.let { itemBean ->
                    val backMoney = if (itemBean.backMoney.isNullOrBlank()) {
                        0.0
                    } else {
                        NumberUtils.numberScale(itemBean.backMoney).toDouble()
                    }
                    if (cashBackAmount <= backMoney) {
                        enableData.add(itemBean)
                    }
                }

            }
        }
        btn_submit.isEnabled = enableData.size > 0
    }

    override fun onRequest(page: Int) {
        val startSN = ed_start_no.text.toString()
        val endSN = ed_end_no.text.toString()
        if (TextUtils.isEmpty(startSN) || TextUtils.isEmpty(endSN) || mMachineType == null) {
            loadData(null)
        } else {
            mPresenter?.fetchMachine(mMachineType?.id
                ?: "", "1", "${startSN},${endSN}", mCashBackAmount ?: "", page)
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_transfer_multi
    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {
        if (mMachineType != null) return
        var type: MachineTypeBean? = null
        data?.let {
            type = it[0]
        }
        bindCheckedMachineType(type)
    }

    override fun bindMachine(data: MyMachineBean?) {
        mMachineCount = if (TextUtils.isEmpty(data?.count)) {
            0
        } else {
            data?.count!!.toInt()
        }
        tv_count.text = "(${data?.count ?: "0"}台)"
        loadData(data?.data)
        setSubmitEnable()
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MachineItemBean?
    ) {
        val cashBackAmount = if (mCashBackAmount.isNullOrBlank()) {
            0.0
        } else {
            mCashBackAmount!!.toDouble()
        }
        val backMoney = if (itemData?.backMoney.isNullOrBlank()) {
            0.0
        } else {
            NumberUtils.numberScale(itemData?.backMoney).toDouble()
        }
        viewHolder?.itemView?.tv_title?.text = "${itemData?.name} ${itemData?.pos_sn}"
        viewHolder?.itemView?.tv_status?.text = "返现金额：${NumberUtils.formatMoney(backMoney)}"
        viewHolder?.itemView?.tv_status?.setTextColor(
            if (cashBackAmount > backMoney) {
                ContextCompat.getColor(requireContext(), R.color.color_ea2618)
            } else {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
        )
        viewHolder?.itemView?.iv_unavailable?.visibility = if (cashBackAmount > backMoney) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun onItemClick(data: MachineItemBean?, position: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SCAN_START_MACHINE || requestCode == RC_SCAN_END_MACHINE) {
                fetchMachineBySN(ScanActivity.getScanResult(data), requestCode)
            }
        }
    }

    private fun fetchMachineBySN(sn: String, requestCode: Int) {
        val type = mMachineType?.id
        if (TextUtils.isEmpty(type)) {
            showToast("请选择机具样式")
            return
        }
        mPresenter?.fetchMachineBySN(type!!, sn, "", DEF_START_PAGE, requestCode)
    }

    override fun bindMachineBySN(data: MyMachineBean?, requestCode: Int) {
        if (data?.data == null || data?.data?.size == 0 || data?.data[0] == null) {
            showToast("编码无效")
            return
        }
        if ("1" != data.data[0].act_status) {
            showToast("该机具已激活")
            return
        }
        if (requestCode == RC_SCAN_START_MACHINE) {
            val startCode = data?.data[0]?.pos_sn
            ed_start_no.setText(startCode)
            ed_start_no.setSelection(
                if (TextUtils.isEmpty(startCode)) {
                    0
                } else {
                    startCode.length
                }
            )
        } else if (requestCode == RC_SCAN_END_MACHINE) {
            val endCode = data?.data[0]?.pos_sn
            ed_end_no.setText(endCode)
            ed_end_no.setSelection(
                if (TextUtils.isEmpty(endCode)) {
                    0
                } else {
                    endCode.length
                }
            )
        }
    }
}