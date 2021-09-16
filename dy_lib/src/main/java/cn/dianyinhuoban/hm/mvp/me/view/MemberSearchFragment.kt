package cn.dianyinhuoban.hm.mvp.me.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.fragment_member_search.*
import kotlinx.android.synthetic.main.item_member.view.*

class MemberSearchFragment : BaseListFragment<Any, IPresenter?>() {

    companion object {
        fun newInstance(): MemberSearchFragment {
            val args = Bundle()
            val fragment = MemberSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.item_member
    }

    override fun getContentView(): Int {
        return R.layout.fragment_member_search
    }

    override fun getPresenter(): IPresenter? {
        return null
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
        Handler().postDelayed({
            var data = mutableListOf<Any>()
            for (i in 1..2) {
                data.add(Any())
            }
            loadData(data)
        }, 1500)
    }


    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: Any?
    ) {
        viewHolder?.itemView?.tv_name?.text = "黄亚华"
        viewHolder?.itemView?.tv_count?.text = "50"
        viewHolder?.itemView?.tv_amount?.text = "75438578.45"
        viewHolder?.itemView?.tv_amount_activation?.text = "788.55"
        viewHolder?.itemView?.tv_amount_transfer?.text = "64334.42"
        viewHolder?.itemView?.tv_inactivated?.text = "64346"
        viewHolder?.itemView?.tv_activated?.text = "699898"
    }

    override fun onItemClick(data: Any?, position: Int) {

    }
}