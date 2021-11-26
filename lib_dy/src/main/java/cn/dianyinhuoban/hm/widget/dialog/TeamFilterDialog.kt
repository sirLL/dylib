package cn.dianyinhuoban.hm.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.bean.MemberLevelBean
import cn.dianyinhuoban.hm.mvp.me.contract.TeamGroupContract
import cn.dianyinhuoban.hm.mvp.me.presenter.TeamGroupPresenter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hjq.toast.ToastUtils
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.AppManager
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.widget.GridSpacingItemDecoration


class TeamFilterDialog : BottomSheetDialogFragment(), TeamGroupContract.View {
    companion object {
        fun newInstance(): TeamFilterDialog {
            val args = Bundle()
            val fragment = TeamFilterDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private var recyclerView: RecyclerView? = null
    private var mCheckedGroupID = ""
    private var mOnFilterCallback: OnFilterCallback? = null
    private val mLevelAdapter: LevelAdapter by lazy {
        LevelAdapter()
    }
    private val mPresenter: TeamGroupContract.Presenter by lazy {
        TeamGroupPresenter(this)
    }

    fun setOnFilterCallback(callback: OnFilterCallback) {
        mOnFilterCallback = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.dy_dialog_team_filter, container, false)
        recyclerView = contentView?.findViewById(R.id.recycler_view)
        recyclerView?.adapter = mLevelAdapter
        recyclerView?.addItemDecoration(GridSpacingItemDecoration(4,
                DimensionUtils.dp2px(requireContext(), 10),
                false))

        contentView.findViewById<TextView>(R.id.tv_reset).setOnClickListener {
            mCheckedGroupID = ""
            mLevelAdapter.notifyDataSetChanged()
        }
        contentView.findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            mOnFilterCallback?.onFilter(mCheckedGroupID)
            dismiss()
        }
        contentView.findViewById<ImageView>(R.id.iv_cancel)?.setOnClickListener {
            dismiss()
        }
        return contentView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPresenter.fetchMemberLevelList()
    }

    override fun bindMemberLevelList(data: List<MemberLevelBean?>?) {
        val levelData = mutableListOf<MemberLevelBean>()
        val all = MemberLevelBean("","全部")
        levelData.add(0, all)
        data?.let {
            for (levelBean in it) {
                levelBean?.let { memberLevelBean ->
                    levelData.add(memberLevelBean)
                }
            }
        }
        mLevelAdapter.data = levelData
    }

    fun show(manager: FragmentManager, tag: String, checkedGroupID: String) {
        mCheckedGroupID = checkedGroupID
        show(manager, tag)
    }

    private fun showToast(message: String) {
        ToastUtils.show(message)
    }

    override fun showMessage(message: String?) {
        message?.let {
            showToast(it)
        }
    }

    override fun onTokenInvalid() {
        AppManager.getInstance().onTokenInvalid(context)
    }

    inner class LevelAdapter : SimpleAdapter<MemberLevelBean>(R.layout.dy_item_dialog_team_filter) {
        override fun convert(viewHolder: SimpleViewHolder?, position: Int, itemData: MemberLevelBean?) {
            val tvValue = viewHolder?.itemView?.findViewById<TextView>(R.id.tv_value)
            tvValue?.text = itemData?.name ?: "--"
            tvValue?.isSelected = if (itemData?.id == null) {
                false
            } else {
                mCheckedGroupID == itemData?.id
            }
        }

        override fun onItemClick(data: MemberLevelBean?, position: Int) {
            data?.id?.let {
                mCheckedGroupID = it
                notifyDataSetChanged()
            }
        }
    }

    interface OnFilterCallback {
        fun onFilter(groupID: String)
    }
}