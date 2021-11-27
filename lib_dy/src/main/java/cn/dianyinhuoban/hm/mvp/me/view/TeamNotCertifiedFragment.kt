package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MemberBean
import cn.dianyinhuoban.hm.mvp.bean.TeamInfoBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamPresenter
import coil.load
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_base.widget.image_view.CircleImageView
import kotlinx.android.synthetic.main.dy_item_team.view.*

class TeamNotCertifiedFragment : BaseListFragment<MemberBean, TeamContract.Presenter?>(),
    TeamContract.View {

    companion object {
        fun newInstance(status: String): TeamNotCertifiedFragment {
            val args = Bundle()
            args.putString("status", status)
            val fragment = TeamNotCertifiedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var status: String? = null

    override fun initView(contentView: View?) {
        super.initView(contentView)
        status = arguments?.getString("status")
        mRefreshLayout.autoRefresh()
    }

    override fun getPresenter(): TeamContract.Presenter? {
        return TeamPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchMyTeam("", "", "", page, status ?: "1")
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_team
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MemberBean?
    ) {
        val ivAvatar = viewHolder?.itemView?.findViewById<CircleImageView>(R.id.iv_avatar)
        ivAvatar?.let {
            it.load(itemData?.avatar) {
                placeholder(R.drawable.dy_img_avatar_def)
                error(R.drawable.dy_img_avatar_def)
                allowHardware(false)
                crossfade(true)
            }
        }
        viewHolder?.itemView?.tv_name?.text = itemData?.name ?: "--"
        viewHolder?.itemView?.tv_date?.text = "注册时间：${itemData?.regtime ?: "--"}"
        viewHolder?.itemView?.tv_total?.text = if (itemData?.teamMonth.isNullOrBlank()) {
            "--"
        } else {
            NumberUtils.formatMoney(itemData?.teamMonth)
        }
        viewHolder?.itemView?.tv_rate?.text = itemData?.rate ?: "--"
        viewHolder?.itemView?.tv_rate?.setTextColor(
            if (itemData?.rate.isNullOrBlank() || itemData?.rate!!.startsWith("-")) {
                ContextCompat.getColor(requireContext(), R.color.color_eab160)
            } else {
                ContextCompat.getColor(requireContext(), R.color.color_c50018)
            }
        )
        viewHolder?.itemView?.findViewById<TextView>(R.id.tv_self)?.visibility =
            if (MMKVUtil.getUserID().isNotBlank() && MMKVUtil.getUserID() == itemData?.uid) {
                View.VISIBLE
            } else {
                View.GONE
            }
    }

    override fun onItemClick(data: MemberBean?, position: Int) {
        data?.uid?.let {
            MemberInfoActivity.openMemberInfoActivity(requireContext(), it)
        }
    }

    override fun bindMyTeam(teamInfoBean: TeamInfoBean?) {
        loadData(teamInfoBean?.list)
    }
}