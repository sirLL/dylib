package cn.dianyinhuoban.hm.mvp.machine.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TransferRecordBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferRecordContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.TransferRecordPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.item_transfer_record.view.*

class TransferRecordFragment : BaseListFragment<TransferRecordBean?, TransferRecordPresenter?>(),
    TransferRecordContract.View {

    companion object {
        fun newInstance(): TransferRecordFragment {
            val args = Bundle()
            val fragment = TransferRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): TransferRecordPresenter? {
        return TransferRecordPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchTransferRecord(page)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_transfer_record
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return super.getItemDecoration()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: TransferRecordBean?
    ) {
        val shouldPay = when (itemData?.type) {
            "1" -> {
                "付款"
            }
            "2" -> {
                "不付款"
            }
            else -> {
                "--"
            }
        }
        viewHolder?.itemView?.tv_name?.text = if (!TextUtils.isEmpty(itemData?.name)) {
            itemData?.name
        } else {
            itemData?.username
        }
        viewHolder?.itemView?.tv_count?.text = "${itemData?.num ?: "--"}台"
        viewHolder?.itemView?.tv_info?.text =
            "$shouldPay  返现金额：${NumberUtils.formatMoney(itemData?.price)}/元"
        viewHolder?.itemView?.tv_date?.text = if (!TextUtils.isEmpty(itemData?.inputTime)) {
            DateTimeUtils.getYYYYMMDDHHMMSS(itemData?.inputTime!!.toLong() * 1000)
        } else {
            "--"
        }
    }

    override fun onItemClick(data: TransferRecordBean?, position: Int) {
        data?.let {
            val intent = Intent(requireContext(), TransferRecordDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("recordID", it.id)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun bindTransferRecord(data: List<TransferRecordBean>?) {
        loadData(data)
    }
}