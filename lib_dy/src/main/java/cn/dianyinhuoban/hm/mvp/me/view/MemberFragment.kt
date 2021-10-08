package cn.dianyinhuoban.hm.mvp.me.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamMemberPresenter
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.item_member.view.*

class MemberFragment : BaseListFragment<TeamMemberBean, TeamMemberPresenter?>(),
    TeamMemberContract.View {

    companion object {
        fun newInstance(): MemberFragment {
            val args = Bundle()
            val fragment = MemberFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): TeamMemberPresenter? {
        return TeamMemberPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchTeamMember("1", "", page)
    }

    override fun bindMemberData(memberData: List<TeamMemberBean>) {
        loadData(memberData)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_member
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: TeamMemberBean?
    ) {
        //名字
        viewHolder?.itemView?.tv_name?.text = when {
            itemData == null -> {
                ""
            }
            TextUtils.isEmpty(itemData.name) -> {
                itemData.username
            }
            else -> {
                itemData.name
            }
        }
        //团队人数
        viewHolder?.itemView?.tv_count?.text = itemData?.inviteNum ?: "--"
        //本月个人交易额
        viewHolder?.itemView?.tv_amount?.text = NumberUtils.formatMoney(itemData?.transTotal)
        //激活码交易量
        viewHolder?.itemView?.tv_amount_activation?.text =
            NumberUtils.formatMoney(itemData?.machineActive)
        //机具交易
        viewHolder?.itemView?.tv_amount_transfer?.text =
            NumberUtils.formatMoney(itemData?.personalTrans)
        //未激活量
        viewHolder?.itemView?.tv_inactivated?.text = itemData?.nonActive ?: "--"
        //已激活量
        viewHolder?.itemView?.tv_activated?.text = itemData?.machineActive ?: "--"
    }

    override fun onItemClick(data: TeamMemberBean?, position: Int) {
        data?.uid?.let {
            MemberInfoActivity.openMemberInfoActivity(requireContext(), it)
        }
    }
}