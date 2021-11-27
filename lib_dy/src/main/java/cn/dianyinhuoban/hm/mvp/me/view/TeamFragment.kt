package cn.dianyinhuoban.hm.mvp.me.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MemberBean
import cn.dianyinhuoban.hm.mvp.bean.TeamInfoBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamPresenter
import cn.dianyinhuoban.hm.widget.dialog.TeamFilterDialog
import cn.dianyinhuoban.hm.widget.dialog.TeamSortDialog
import coil.load
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_base.widget.DividerDecoration
import com.wareroom.lib_base.widget.image_view.CircleImageView
import kotlinx.android.synthetic.main.dy_item_team.view.*
import kotlinx.android.synthetic.main.dy_fragment_team.*

class TeamFragment : BaseListFragment<MemberBean, TeamContract.Presenter?>(), TeamContract.View {
    companion object {
        fun newInstance(): TeamFragment {
            val args = Bundle()
            val fragment = TeamFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mAmountSort = TeamSortDialog.SORT_DOWN
    private var mDateSort = ""
    private var mGroupID = ""


    override fun getContentView(): Int {
        return R.layout.dy_fragment_team
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_team
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerDecoration(ContextCompat.getColor(requireContext(), R.color.dy_color_divider), 0)
    }

    override fun getRefreshHeader(): RefreshHeader {
        return ClassicsHeader(requireContext()).setAccentColor(Color.WHITE)
    }

    override fun getLoadMoreFooter(): RefreshFooter {
        return ClassicsFooter(requireContext()).setPrimaryColor(ContextCompat.getColor(requireContext(), R.color.dy_base_color_page_bg))
    }

    override fun getPresenter(): TeamContract.Presenter? {
        return TeamPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchMyTeam(mAmountSort, mDateSort, mGroupID, page, "")
    }

    override fun bindMyTeam(teamInfoBean: TeamInfoBean?) {
        tv_member_total?.text = teamInfoBean?.total ?: "--"
        tv_verified?.text = teamInfoBean?.auth ?: "--"
        tv_not_certified?.text = teamInfoBean?.nonAuth ?: "--"
        loadData(teamInfoBean?.list)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        contentView?.findViewById<TextView>(R.id.textView3)?.setOnClickListener {
            TeamNotCertifiedActivity.open(requireContext(),"1")
        }
        contentView?.findViewById<TextView>(R.id.textView2)?.setOnClickListener {
            TeamNotCertifiedActivity.open(requireContext(),"2")
        }
        contentView?.findViewById<TextView>(R.id.tv_sort)?.setOnClickListener {
            showSortDialog()
        }
        contentView?.findViewById<TextView>(R.id.tv_filter)?.setOnClickListener {
            showFilterDialog()
        }
        mRefreshLayout.autoRefresh()
    }

    override fun convert(viewHolder: SimpleAdapter.SimpleViewHolder?, position: Int, itemData: MemberBean?) {
        val ivAvatar=viewHolder?.itemView?.findViewById<CircleImageView>(R.id.iv_avatar)
        ivAvatar?.let {
            it.load(itemData?.avatar){
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
        viewHolder?.itemView?.tv_rate?.setTextColor(if (itemData?.rate.isNullOrBlank() || itemData?.rate!!.startsWith("-")) {
            ContextCompat.getColor(requireContext(), R.color.color_eab160)
        } else {
            ContextCompat.getColor(requireContext(), R.color.color_c50018)
        })
        viewHolder?.itemView?.findViewById<TextView>(R.id.tv_self)?.visibility = if (MMKVUtil.getUserID().isNotBlank() && MMKVUtil.getUserID() == itemData?.uid) {
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

    private fun showSortDialog() {
        val dialog = TeamSortDialog.newInstance()
        dialog.setOnSortCallBack(object : TeamSortDialog.OnSortCallBack {
            override fun onSort(amountSort: String, dateSort: String) {
                mAmountSort = amountSort
                mDateSort = dateSort
                mRefreshLayout?.autoRefresh()
            }
        })
        dialog.show(childFragmentManager, "TeamSortDialog", mAmountSort, mDateSort)
    }

    private fun showFilterDialog() {
        val dialog = TeamFilterDialog.newInstance()
        dialog.setOnFilterCallback(object : TeamFilterDialog.OnFilterCallback {
            override fun onFilter(groupID: String) {
                mGroupID = groupID
                mRefreshLayout.autoRefresh()
            }
        })
        dialog.show(childFragmentManager, "TeamFilterDialog", mGroupID)
    }
}