package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamMemberPresenter
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_fragment_member_search.*
import kotlinx.android.synthetic.main.dy_item_member.view.*

class MemberSearchFragment : BaseListFragment<TeamMemberBean, TeamMemberPresenter?>(), TeamMemberContract.View {

    companion object {
        fun newInstance(): MemberSearchFragment {
            val args = Bundle()
            val fragment = MemberSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_member
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_member_search
    }

    override fun getPresenter(): TeamMemberPresenter? {
        return TeamMemberPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val keyword = ed_search.text.toString()
                if (keyword.isNotEmpty()) {
                    iv_clean.visibility = View.VISIBLE
                } else {
                    iv_clean.visibility = View.INVISIBLE
                }
            }
        })
        ed_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else {
                false
            }
        }
        tv_cancel.setOnClickListener {
            activity?.finish()
        }
        iv_clean.setOnClickListener {
            ed_search.setText("")
        }
    }

    private fun doSearch() {
        mCurrentPage = DEF_START_PAGE
        showLoadingView()
        onRequest(mCurrentPage)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchTeamMember("1", ed_search.text.toString(), page)
    }

    override fun bindMemberData(memberData: List<TeamMemberBean>) {
        loadData(memberData)
    }


    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: TeamMemberBean?
    ) {
        viewHolder?.itemView?.tv_name?.text = if (itemData != null) {
            if (itemData.name.isNullOrBlank()) {
                itemData.username
            } else {
                itemData.name
            }
        } else {
            "--"
        }
        viewHolder?.itemView?.tv_count?.text = if (itemData != null) {
            itemData.inviteNum
        } else {
            "--"
        }
        viewHolder?.itemView?.tv_amount?.text = if (itemData != null) {
            NumberUtils.formatMoney(itemData.transTotal)
        } else {
            "--"
        }
//        viewHolder?.itemView?.tv_amount_activation?.text = "788.55"
        viewHolder?.itemView?.tv_amount_transfer?.text = if (itemData != null) {
            NumberUtils.formatMoney(itemData.personalTrans)
        } else {
            "--"
        }
        viewHolder?.itemView?.tv_inactivated?.text = if (itemData != null) {
            itemData.nonActive
        } else {
            "--"
        }
        viewHolder?.itemView?.tv_activated?.text = if (itemData != null) {
            itemData.machineActive
        } else {
            "--"
        }
    }

    override fun onItemClick(data: TeamMemberBean?, position: Int) {
        data?.uid?.let {
            MemberInfoActivity.openMemberInfoActivity(requireContext(), it)
        }
    }
}