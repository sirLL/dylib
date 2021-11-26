package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineItemBean
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineTypePresenter
import cn.dianyinhuoban.hm.mvp.machine.presenter.TransferPresenter
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.filter.NumberFilter
import kotlinx.android.synthetic.main.dy_activity_transfer.*
import kotlinx.android.synthetic.main.dy_item_transfer.view.*

class TransferActivity : BaseActivity<TransferPresenter?>(), TransferContract.View {

    companion object {
        private const val RC_MEMBER_PICKER = 1023
        private const val RC_MACHINE_PICKER = 1024
        private const val RC_MULTI_PICKER = 1025

        const val PICKER_TYPE_SELECTOR = "PICKER_TYPE_SELECTOR"
        const val PICKER_TYPE_MULTI = "PICKER_TYPE_MULTI"
    }

    private var mMachineType: MachineTypeBean? = null
    private var mMember: TeamMemberBean? = null
    private var mAdapter: SimpleAdapter<MachineItemBean>? = null

    private val mMachineTypePicker: MachineTypePicker by lazy {
        MachineTypePicker.newInstance("-1")
    }

    private val mShouldPayPicker: ShouldPayPicker by lazy {
        ShouldPayPicker.newInstance()
    }

    override fun getPresenter(): TransferPresenter? {
        return TransferPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dy_activity_transfer)
        setTitle("划拨")

        setupRecyclerView()
        setupClickAction()
        ed_cash_back.filters = arrayOf(NumberFilter().setDigits(2))
        btn_submit.setOnClickListener {
            submitTransfer()
        }
        ed_cash_back.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                mAdapter?.notifyDataSetChanged()
                setSubmitEnable()
            }

        })
    }

    private fun setSubmitEnable() {
        val backAmount = ed_cash_back.text.toString()
        val enableData = mutableListOf<MachineItemBean>()
        val cashBackAmount = if (!backAmount.isNullOrBlank()) {
            backAmount!!.toDouble()
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

        tv_count.text = "(${enableData.size}台)"

        btn_submit.isEnabled =  mMember != null && mMachineType != null
                && enableData.size > 0
                && !TextUtils.isEmpty(ed_cash_back.text.toString())

    }

    private fun setupRecyclerView() {
        mAdapter = object : SimpleAdapter<MachineItemBean>(R.layout.dy_item_transfer) {
            override fun convert(
                viewHolder: SimpleViewHolder?,
                position: Int,
                itemData: MachineItemBean?,
            ) {
                viewHolder?.itemView?.tv_title?.text = "${itemData?.name} ${itemData?.pos_sn}"
                val cashBackAmount = ed_cash_back.text.toString()
                var cashBackAmountDouble = 0.0
                if (!cashBackAmount.isNullOrBlank()) {
                    cashBackAmountDouble = cashBackAmount.toDouble()
                }
                val backMoney = if (itemData?.backMoney.isNullOrBlank()) {
                    0.0
                } else {
                    NumberUtils.numberScale(itemData?.backMoney).toDouble()
                }
                viewHolder?.itemView?.tv_status?.text = "返现金额：${NumberUtils.formatMoney(backMoney)}"
                viewHolder?.itemView?.tv_status?.setTextColor(
                    if (cashBackAmountDouble > backMoney) {
                        ContextCompat.getColor(this@TransferActivity, R.color.color_ea2618)
                    } else {
                        ContextCompat.getColor(this@TransferActivity, R.color.color_999999)
                    }
                )
                viewHolder?.itemView?.iv_unavailable?.visibility = if (cashBackAmountDouble > backMoney) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
                viewHolder?.itemView?.iv_delete?.setOnClickListener {
                    if (data != null && data.size > position) {
                        data?.removeAt(position)
                        notifyItemRemoved(position)
                        setSubmitEnable()
                    }
                }
            }

            override fun onItemClick(data: MachineItemBean?, position: Int) {

            }
        }
        recycler_view.adapter = mAdapter
    }

    private fun setupClickAction() {
        //选择划拨人员
        cl_member_container.setOnClickListener {
            MemberPickerActivity.openMemberPicker(
                TransferActivity@ this,
                mMember?.uid ?: "",
                "选择划拨人员",
                RC_MEMBER_PICKER
            )
        }
        //选择机具样式
        action_view_machine.setOnClickListener {
            mMachineTypePicker.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<MachineTypeBean?, MachineTypePresenter?> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<MachineTypeBean?, MachineTypePresenter?>,
                    t: MachineTypeBean?,
                    position: Int,
                ) {
                    t?.let {
                        bottomPicker.dismiss()
                        bindCheckedMachineType(it)
                    }
                }
            })
            mMachineTypePicker.setCheckedID(mMachineType?.id)
            mMachineTypePicker.show(supportFragmentManager, "MachineTypePicker")
        }

        //批量划分
        tv_tab_multi.setOnClickListener {
            if (mMachineType == null) {
                showToast("请选择机具样式")
                return@setOnClickListener
            }
            val cashBackAmount = ed_cash_back.text.toString()
            if (cashBackAmount.isBlank()) {
                showToast("请输入返现金额")
                return@setOnClickListener
            } else if (cashBackAmount.toDouble() < 0) {
                showToast("返现金额必须大于等于0")
                return@setOnClickListener
            }
            TransferMultiActivity.openTransferMultiActivity(this, mMachineType, cashBackAmount, RC_MULTI_PICKER)
        }

        //选择划分
        tv_tab_select.setOnClickListener {
            if (mMachineType == null) {
                showToast("请选择机具样式")
                return@setOnClickListener
            }
            val cashBackAmount = ed_cash_back.text.toString()
            if (cashBackAmount.isBlank()) {
                showToast("请输入返现金额")
                return@setOnClickListener
            } else if (cashBackAmount.toDouble() < 0) {
                showToast("返现金额必须大于等于0")
                return@setOnClickListener
            }

            val checkedData = mAdapter?.data
            val checkedIDList = arrayListOf<String>()
            checkedData?.let {
                for (machine in it) {
                    machine?.let { machine_ ->
                        checkedIDList.add(machine_.id)
                    }
                }
            }
            MachinePickerActivity.openMachinePicker(
                this,
                mMachineType!!,
                checkedIDList,
                cashBackAmount,
                RC_MACHINE_PICKER
            )
        }
    }

    private fun bindCheckedMachineType(machineType: MachineTypeBean?) {
        mMachineType = machineType
        tv_machine.text = machineType?.name ?: ""
        setSubmitEnable()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_MEMBER_PICKER -> {
                    val member = data?.extras?.getParcelable<TeamMemberBean>("member")
                    bindCheckedMember(member)
                }
                RC_MACHINE_PICKER -> {
//                    val pickerType = data?.extras?.getString("pickerType", "")
//                    if (pickerType == PICKER_TYPE_MULTI) {
//                        showMultiResult(data)
//                    } else {
//                        val checkedData =
//                                data?.extras?.getParcelableArrayList<MachineItemBean>("checkedData")
//                        val machineType = data?.extras?.getParcelable<MachineTypeBean>("type")
//                        bindCheckedMachineType(machineType)
//                        showSelectedMachine(checkedData)
//                    }
                    val checkedData =
                        data?.extras?.getParcelableArrayList<MachineItemBean>("checkedData")
                    val machineType = data?.extras?.getParcelable<MachineTypeBean>("type")
                    bindCheckedMachineType(machineType)
                    showSelectedMachine(checkedData)
                }
                RC_MULTI_PICKER -> {
//                    showMultiResult(data)
                    val checkedData =
                        data?.extras?.getParcelableArrayList<MachineItemBean>("checkedData")
                    val machineType = data?.extras?.getParcelable<MachineTypeBean>("type")
                    bindCheckedMachineType(machineType)
                    showSelectedMachine(checkedData)
                }
            }
        }
    }

    private var mStartSN: String? = null
    private var mEndSN: String? = null

    private fun showMultiResult(data: Intent?) {
        recycler_view.visibility = View.GONE
        cl_multi_container.visibility = View.VISIBLE
        mAdapter?.data = null

        mStartSN = data?.extras?.getString("startSN")
        mEndSN = data?.extras?.getString("endSN")
        val machineCount = data?.extras?.getInt("machineCount", 0)
        tv_start_no.text = mStartSN
        tv_end_no.text = mEndSN
        tv_count.text = "(${machineCount ?: 0}台)"
        setSubmitEnable()
        bindCheckedMachineType(data?.extras?.getParcelable("type"))
    }

    private fun showSelectedMachine(machineData: List<MachineItemBean>?) {
        recycler_view.visibility = View.VISIBLE
        cl_multi_container.visibility = View.GONE
        tv_start_no.text = ""
        tv_end_no.text = ""
        mStartSN = ""
        mEndSN = ""

        mAdapter?.data = machineData
        tv_count.text = "(${mAdapter?.data?.size ?: "0"}台)"
        setSubmitEnable()
    }

    private fun bindCheckedMember(member: TeamMemberBean?) {
        member?.let {
            mMember = it
            tv_name.text = if (!TextUtils.isEmpty(it.name)) {
                it.name
            } else {
                it.username
            }
            tv_inactivated.text = it.nonActive
            setSubmitEnable()
        }
    }

    private fun submitTransfer() {
        val backAmount = ed_cash_back.text.toString()
        val enableData = mutableListOf<MachineItemBean>()
        val cashBackAmount = if (!backAmount.isNullOrBlank()) {
            backAmount!!.toDouble()
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

        var transferType = "1"
        val builder = StringBuilder()
        for (machine in enableData) {
            if (builder.isNotEmpty()) {
                builder.append(",")
            }
            builder.append(machine.id)
        }
        val checkedIDs = builder.toString()
        mPresenter?.submitMachineTransfer(
            mMember?.uid ?: "",
            mMachineType?.id ?: "",
            "",
            NumberUtils.numberScale(backAmount),
            checkedIDs,
            transferType,
            "",
            ""
        )
    }

    override fun onSubmitMachineTransferSuccess() {
        showToast("划拨成功")
        finish()
    }
}