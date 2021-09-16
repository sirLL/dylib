package cn.dianyinhuoban.hm.mvp.machine.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.PopupWindowCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineItemBean
import cn.dianyinhuoban.hm.mvp.bean.MachineStatusBean
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineManagerContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineManagerPresenter
import cn.dianyinhuoban.hm.mvp.order.SubmitOrderActivity
import cn.dianyinhuoban.hm.mvp.order.view.ProductListActivity
import cn.dianyinhuoban.hm.widget.pop.PopListPicker
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnMultiListener
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.widget.DividerDecoration
import kotlinx.android.synthetic.main.fragment_machine_manager.*
import kotlinx.android.synthetic.main.item_pop_list_picker.view.*
import kotlinx.android.synthetic.main.item_machine_manager.view.*

class MachineManagerFragment : BaseListFragment<MachineItemBean, MachineManagerPresenter?>(),
    MachineManagerContract.View {

    private var mMachineType: MachineTypeBean? = null
    private var mMachineStatus: MachineStatusBean? = null
    private var mMachineTypePicker: PopListPicker<MachineTypeBean>? = null
    private var mStatusPicker: PopListPicker<MachineStatusBean>? = null

    companion object {
        fun newInstance(): MachineManagerFragment {
            val args = Bundle()
            val fragment = MachineManagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.item_machine_manager
    }

    override fun getContentView(): Int {
        return R.layout.fragment_machine_manager
    }

    override fun getPresenter(): MachineManagerPresenter? {
        return MachineManagerPresenter(this)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerDecoration(
            Color.TRANSPARENT,
            DimensionUtils.dp2px(context, 10)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ll_tab_product.setOnClickListener {
            showTypePicker()
        }
        ll_tab_status.setOnClickListener {
            showStatusPicker()
        }
        fl_tab_purchase.setOnClickListener {
            startActivity(Intent(context, ProductListActivity::class.java))
        }
        fl_tab_transfer.setOnClickListener {
            startActivity(Intent(context, MachineTransferActivity::class.java))
        }
        mMachineType = MachineTypeBean("", "全部产品")
        mMachineStatus = MachineStatusBean("", "全部")
        fetchMachineType()

        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        mRefreshLayout.setOnMultiListener(object : OnMultiListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                ll_tab_product.isEnabled = false
                ll_tab_status.isEnabled = false
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ll_tab_product.isEnabled = false
                ll_tab_status.isEnabled = false
            }

            override fun onStateChanged(
                refreshLayout: RefreshLayout,
                oldState: RefreshState,
                newState: RefreshState
            ) {

            }

            override fun onHeaderMoving(
                header: RefreshHeader?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                headerHeight: Int,
                maxDragHeight: Int
            ) {

            }

            override fun onHeaderReleased(
                header: RefreshHeader?,
                headerHeight: Int,
                maxDragHeight: Int
            ) {

            }

            override fun onHeaderStartAnimator(
                header: RefreshHeader?,
                headerHeight: Int,
                maxDragHeight: Int
            ) {

            }

            override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
                ll_tab_product.isEnabled = true
                ll_tab_status.isEnabled = true
            }

            override fun onFooterMoving(
                footer: RefreshFooter?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                footerHeight: Int,
                maxDragHeight: Int
            ) {

            }

            override fun onFooterReleased(
                footer: RefreshFooter?,
                footerHeight: Int,
                maxDragHeight: Int
            ) {

            }

            override fun onFooterStartAnimator(
                footer: RefreshFooter?,
                footerHeight: Int,
                maxDragHeight: Int
            ) {

            }

            override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {
                ll_tab_product.isEnabled = true
                ll_tab_status.isEnabled = true
            }

        })
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchMachine(mMachineType?.id ?: "", mMachineStatus?.id ?: "", "", page)
    }

    override fun bindMachine(data: MyMachineBean?) {
        tv_all_count.text = "全部机器 ${data?.count ?: "--"}"
        tv_inactivated_count.text = "未激活 ${data?.nonActiveCount ?: "--"}"
        tv_activated_count.text = "已激活 ${data?.activeCount ?: "--"}"
        loadData(data?.data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MachineItemBean?
    ) {
        viewHolder?.itemView?.tv_machine_title?.text = itemData?.name ?: "--"
        viewHolder?.itemView?.tv_sn?.text = itemData?.pos_sn ?: "--"
        viewHolder?.itemView?.tv_date?.text =
            DateTimeUtils.getYYYYMMDDHHMMSS((itemData?.act_time?.toLong() ?: 0) * 1000)
        viewHolder?.itemView?.tv_status?.text = if ("1" == itemData?.act_status) {
            "未激活"
        } else if ("2" == itemData?.act_status || "3" == itemData?.act_status || "2,3" == itemData?.act_status) {
            "已激活"
        } else {
            "--"
        }
    }

    override fun onItemClick(data: MachineItemBean?, position: Int) {
        data?.let {
            val intent = Intent(context, TradeRecordActivity::class.java)
            val bundle = Bundle()
            bundle.putString("sn", data.pos_sn)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }


    /******************************************  机具类型START  *****************************************/
    private fun onMachineTypeChecked(machineType: MachineTypeBean) {
        mMachineType = machineType
        tv_tab_product.text = machineType.name
        refresh_layout.autoRefresh()
    }

    private fun initTypePicker() {
        if (mMachineTypePicker == null) {
            mMachineTypePicker = object : PopListPicker<MachineTypeBean>(requireContext()) {
                override fun convert(
                    viewHolder: SimpleAdapter.SimpleViewHolder?,
                    position: Int,
                    itemData: MachineTypeBean
                ) {
                    viewHolder?.itemView?.tv_title?.text = itemData?.name
                    mMachineType?.let {
                        viewHolder?.itemView?.tv_title?.isSelected = (it.id == itemData?.id)
                    }
                }

                override fun onItemChecked(
                    pop: PopListPicker<MachineTypeBean>,
                    data: MachineTypeBean,
                    position: Int
                ) {
                    pop.dismiss()
                    onMachineTypeChecked(data)
                }
            }

        }
    }

    private fun showTypePicker() {
        initTypePicker()
        val arr = IntArray(2)
        ll_menu_tab_container.getLocationOnScreen(arr)
        mMachineTypePicker?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mMachineTypePicker?.height =
            DimensionUtils.getScreenHeight(requireContext()) - arr[1] - ll_menu_tab_container.height - 2
        mMachineTypePicker?.notifyDataSetChanged()
        PopupWindowCompat.showAsDropDown(
            mMachineTypePicker!!,
            ll_menu_tab_container,
            0,
            2,
            Gravity.BOTTOM
        )
    }

    private fun fetchMachineType() {
        mPresenter?.fetchMachineType()
    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {
        initTypePicker()
        val typeData = mutableListOf<MachineTypeBean>()
        typeData.add(MachineTypeBean("", "全部产品"))
        data?.let {
            typeData.addAll(it)
        }
        mMachineTypePicker?.setData(typeData)
    }
    /******************************************  机具类型END  *****************************************/

    /******************************************  机具状态START  *****************************************/
    private fun initStatusPicker() {
        if (mStatusPicker == null) {
            mStatusPicker = object : PopListPicker<MachineStatusBean>(requireContext()) {
                override fun convert(
                    viewHolder: SimpleAdapter.SimpleViewHolder?,
                    position: Int,
                    itemData: MachineStatusBean
                ) {
                    viewHolder?.itemView?.tv_title?.text = itemData?.name
                    mMachineStatus?.let {
                        viewHolder?.itemView?.tv_title?.isSelected = (it.id == itemData?.id)
                    }
                }

                override fun onItemChecked(
                    pop: PopListPicker<MachineStatusBean>,
                    data: MachineStatusBean,
                    position: Int
                ) {
                    pop.dismiss()
                    onStatusChecked(data)
                }
            }

            val statusData = mutableListOf<MachineStatusBean>()
            statusData.add(MachineStatusBean("", "全部"))
            statusData.add(MachineStatusBean("2,3", "已激活"))
            statusData.add(MachineStatusBean("1", "未激活"))
            mStatusPicker?.setData(statusData)
        }
    }

    private fun showStatusPicker() {
        initStatusPicker()
        val arr = IntArray(2)
        ll_menu_tab_container.getLocationOnScreen(arr)
        mStatusPicker?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mStatusPicker?.height =
            DimensionUtils.getScreenHeight(requireContext()) - arr[1] - ll_menu_tab_container.height - 2
        mStatusPicker?.notifyDataSetChanged()
        PopupWindowCompat.showAsDropDown(
            mStatusPicker!!,
            ll_menu_tab_container,
            0,
            2,
            Gravity.BOTTOM
        )
    }

    private fun onStatusChecked(machineStatus: MachineStatusBean) {
        mMachineStatus = machineStatus
        tv_tab_status.text = mMachineStatus?.name
        refresh_layout.autoRefresh()
    }
    /******************************************  机具状态END  *****************************************/
}