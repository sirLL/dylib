package cn.dianyinhuoban.hm.mvp.machine.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MyMachineBean
import cn.dianyinhuoban.hm.mvp.bean.TeamMachineItemBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MyMachineContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MyMachinePresenter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.widget.LoadingLayout
import kotlinx.android.synthetic.main.activity_machine_transfer.*
import kotlinx.android.synthetic.main.item_machine_transfer.view.*
import java.math.BigDecimal

class MachineTransferActivity : BaseActivity<MyMachinePresenter?>(), OnRefreshListener,
    OnLoadMoreListener,
    LoadingLayout.OnViewClickListener, MyMachineContract.View {

    companion object {
        private const val DEF_START_PAGE = 1
    }

    private var isLoadMore = false
    private var mCurrentPage = DEF_START_PAGE
    private var mAdapter: SimpleAdapter<TeamMachineItemBean>? = null

    override fun getPresenter(): MyMachinePresenter? {
        return MyMachinePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_machine_transfer)
        setTitle("机具划拨")

        setRightButtonText("划拨明细") {
            startActivity(Intent(this, TransferRecordActivity::class.java))
        }

        tv_transfer.setOnClickListener {
            startActivity(Intent(MachineTransferActivity@ this, TransferActivity::class.java))
        }
        setupRecyclerView()
        setupRefreshLayout()
        setupLoadLayout()

        loading_layout.showLoading()
        request()
    }

    private fun setupRecyclerView() {
        mAdapter = object : SimpleAdapter<TeamMachineItemBean>(R.layout.item_machine_transfer) {
            override fun convert(
                viewHolder: SimpleViewHolder?,
                position: Int,
                itemData: TeamMachineItemBean?
            ) {
                viewHolder?.itemView?.tv_name?.text = if (!TextUtils.isEmpty(itemData?.name)) {
                    itemData?.name
                } else {
                    itemData?.username ?: "--"
                }
                viewHolder?.itemView?.tv_activation_amount?.text =
                    "${itemData?.machineActive ?: "--"}/${itemData?.machineTotal ?: "--"}"

                val active = NumberUtils.string2BigDecimal(itemData?.machineActive ?: "0")
                val total = NumberUtils.string2BigDecimal(itemData?.machineTotal ?: "0")
                viewHolder?.itemView?.pb_amount_activation?.progress = if (total.toDouble() <= 0) {
                    0
                } else {
                    active.divide(total, 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).toInt()
                }
            }

            override fun onItemClick(data: TeamMachineItemBean?, position: Int) {

            }

        }
        recycler_view.adapter = mAdapter
    }

    private fun setupRefreshLayout() {
        refresh_layout.setRefreshFooter(ClassicsFooter(MachineTransferActivity@ this))
        refresh_layout.setRefreshHeader(ClassicsHeader(MachineTransferActivity@ this))
        refresh_layout.setOnLoadMoreListener(MachineTransferActivity@ this)
        refresh_layout.setOnRefreshListener(MachineTransferActivity@ this)
        refresh_layout.setEnableLoadMore(false)
    }

    private fun setupLoadLayout() {
        loading_layout.setOnViewClickListener(MachineTransferActivity@ this)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        isLoadMore = false
        mCurrentPage = DEF_START_PAGE
        request()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isLoadMore = true
        request()
    }

    override fun onReloadClick() {
        isLoadMore = false
        request()
    }

    private fun loadData(data: List<TeamMachineItemBean>?) {
        if (isLoadMore) {
            mAdapter?.appendData(data)
            refresh_layout.finishLoadMore()
        } else {
            mAdapter?.data = data
            refresh_layout.finishRefresh()
            if (data == null || data.isEmpty()) {
                loading_layout.showEmpty()
                refresh_layout.setEnableLoadMore(false)
            } else {
                mCurrentPage += 1
                loading_layout.showSuccess()
                refresh_layout.setEnableLoadMore(true)
            }
        }
    }

    private fun request() {
        mPresenter?.fetchMachine("", "", "", 1)
        mPresenter?.fetchTeamMachine("", mCurrentPage)
    }

    override fun bindMachine(data: MyMachineBean?) {
        tv_amount_transfer.text = data?.nonActiveCount ?: "--"
        tv_amount_all.text = data?.count ?: "--"
        tv_amount_activation.text = data?.activeCount ?: "--"
    }

    override fun bindTeamMachine(data: List<TeamMachineItemBean>?) {
        loadData(data)
    }
}