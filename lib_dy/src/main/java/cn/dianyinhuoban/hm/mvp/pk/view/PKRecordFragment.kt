package cn.dianyinhuoban.hm.mvp.pk.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PKRecordBean
import cn.dianyinhuoban.hm.mvp.pk.contract.PKRecordContract
import cn.dianyinhuoban.hm.mvp.pk.presenter.PKRecordPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import kotlinx.android.synthetic.main.item_pk_record.view.*

class PKRecordFragment : BaseListFragment<PKRecordBean, PKRecordPresenter>(),
    PKRecordContract.View {

    companion object {
        fun newInstance(): PKRecordFragment {
            val args = Bundle()
            val fragment = PKRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): PKRecordPresenter {
        return PKRecordPresenter(this)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_pk_record
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.fetchPKRecord(DEF_START_PAGE)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchPKRecord(page)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PKRecordBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = when (itemData?.type) {
            "1" -> {
                "个人PK"
            }
            "2" -> {
                "团队PK"
            }
            else -> {
                "--"
            }
        }
        viewHolder?.itemView?.tv_status?.text = when (itemData?.status) {
            //状态 1 发起 2 接受 3 拒绝 4.完成
            "1" -> {
                viewHolder?.itemView?.tv_result?.visibility = View.GONE
                viewHolder?.itemView?.divider1?.visibility = View.GONE
                "待接受"
            }
            "2" -> {
                viewHolder?.itemView?.tv_result?.visibility = View.GONE
                viewHolder?.itemView?.divider1?.visibility = View.GONE
                "PK中…"
            }
            "3" -> {
                viewHolder?.itemView?.tv_result?.visibility = View.GONE
                viewHolder?.itemView?.divider1?.visibility = View.GONE
                "已拒绝"
            }
            "4" -> {
                viewHolder?.itemView?.tv_result?.visibility = View.VISIBLE
                viewHolder?.itemView?.divider1?.visibility = View.VISIBLE
                if ("1" == itemData?.isWin) {
                    viewHolder?.itemView?.tv_result?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_3797ff
                        )
                    )
                    viewHolder?.itemView?.tv_result?.text = "成功"
                } else {
                    viewHolder?.itemView?.tv_result?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_c50018
                        )
                    )
                    viewHolder?.itemView?.tv_result?.text = "失败"
                }

                "PK结束"
            }
            else -> {
                viewHolder?.itemView?.tv_result?.visibility = View.GONE
                viewHolder?.itemView?.divider1?.visibility = View.GONE
                "--"
            }
        }
        viewHolder?.itemView?.tv_my_team?.text = MMKVUtil.getTeam()
        viewHolder?.itemView?.tv_other_team?.text = itemData?.name
        viewHolder?.itemView?.tv_amount?.text = if (TextUtils.isEmpty(itemData?.reward)) {
            "--"
        } else {
            NumberUtils.formatMoney(itemData?.reward)
        }
        viewHolder?.itemView?.tv_cycle?.text = "PK周期    ${itemData?.cycle ?: "--"}天"

        val startTime = DateTimeUtils.formatDate(
            (itemData?.startTime?.toLong() ?: 0) * 1000,
            DateTimeUtils.PATTERN_MM_DD_HH_MM
        )
        val endTime = DateTimeUtils.formatDate(
            (itemData?.endTime?.toLong() ?: 0) * 1000,
            DateTimeUtils.PATTERN_MM_DD_HH_MM
        )
        viewHolder?.itemView?.tv_time?.text = "PK时间    $startTime — $endTime"
    }

    override fun onItemClick(data: PKRecordBean?, position: Int) {

    }

    override fun bindPKRecord(data: List<PKRecordBean>?) {
        loadData(data)
    }
}