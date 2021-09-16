package cn.dianyinhuoban.hm.mvp.machine.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.TeamMemberBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamMemberContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamMemberPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.fragment_member_picker.ed_search
import kotlinx.android.synthetic.main.fragment_member_picker.iv_clean
import kotlinx.android.synthetic.main.fragment_member_picker.tv_cancel
import kotlinx.android.synthetic.main.item_member_picker.view.*

class MemberPickerFragment : BaseListFragment<TeamMemberBean?, TeamMemberPresenter?>(),
    TeamMemberContract.View {
    private var mCheckedID = "-1"

    companion object {
        fun newInstance(checkedID: String): MemberPickerFragment {
            val args = Bundle()
            val fragment = MemberPickerFragment()
            args.putString("checkedID", checkedID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun getContentView(): Int {
        return R.layout.fragment_member_picker
    }

    override fun getItemLayout(): Int {
        return R.layout.item_member_picker
    }

    override fun getPresenter(): TeamMemberPresenter? {
        return TeamMemberPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val checkedID = bundle?.getString("checkedID", "-1")
        mCheckedID = checkedID ?: "-1"
        ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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
                showLoadingView()
                onRequest(DEF_START_PAGE)
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
        onRequest(DEF_START_PAGE)
    }

    override fun onItemClick(data: TeamMemberBean?, position: Int) {
        data?.uid?.let {
            mCheckedID = it
            mAdapter.notifyDataSetChanged()
            activity?.let { ac ->
                val intent = Intent()
                val bundle = Bundle()
                bundle.putParcelable("member", data)
                intent.putExtras(bundle)
                ac.setResult(RESULT_OK, intent)
                ac.finish()
            }
        }
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchTeamMember("1", ed_search.text.toString(), page)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: TeamMemberBean?
    ) {
        viewHolder?.itemView?.tv_name?.text = if (!TextUtils.isEmpty(itemData?.name)) {
            itemData?.name
        } else {
            itemData?.username ?: "--"
        }

        viewHolder?.itemView?.tv_inactivated?.text =
            "未激活数量：${itemData?.nonActive ?: "--"}台"
        viewHolder?.itemView?.iv_check_box?.isSelected = if (itemData != null) {
            mCheckedID == itemData?.uid
        } else {
            false
        }
    }

    override fun bindMemberData(memberData: List<TeamMemberBean>) {
        loadData(memberData)
    }


}