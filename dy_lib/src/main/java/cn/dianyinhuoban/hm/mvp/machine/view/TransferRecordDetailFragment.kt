package cn.dianyinhuoban.hm.mvp.machine.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TransferRecordDetailBean
import cn.dianyinhuoban.hm.mvp.machine.contract.TransferRecordDetailContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.TransferRecordDetailPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import kotlinx.android.synthetic.main.dy_item_transfer_record_detail.view.*

class TransferRecordDetailFragment :
    BaseListFragment<TransferRecordDetailBean?, TransferRecordDetailPresenter?>(),
    TransferRecordDetailContract.View {
    companion object {
        fun newInstance(recordID: String?): TransferRecordDetailFragment {
            val args = Bundle()
            args.putString("recordID", recordID)
            val fragment = TransferRecordDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mRecordID: String? = null

    override fun getPresenter(): TransferRecordDetailPresenter? {
        return TransferRecordDetailPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRecordID = arguments?.getString("recordID", "")
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchTransferRecordDetail(mRecordID ?: "", page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_transfer_record_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: TransferRecordDetailBean?
    ) {
        viewHolder?.itemView?.tv_title?.text =
            "${itemData?.posTypeName ?: "--"} ${itemData?.posSn ?: "--"}"
        viewHolder?.itemView?.tv_status?.text = if (!TextUtils.isEmpty(itemData?.inputTime)) {
            DateTimeUtils.getYYYYMMDDHHMMSS(itemData?.inputTime!!.toLong() * 1000)
        } else {
            "--"
        }
    }

    override fun onItemClick(data: TransferRecordDetailBean?, position: Int) {

    }

    override fun bindRecordDetail(data: List<TransferRecordDetailBean>?) {
        loadData(data)
    }
}