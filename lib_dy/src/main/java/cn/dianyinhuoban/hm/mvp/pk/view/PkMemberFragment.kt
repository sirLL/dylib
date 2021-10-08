package cn.dianyinhuoban.hm.mvp.pk.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PkMember
import cn.dianyinhuoban.hm.mvp.pk.contract.PKContract
import cn.dianyinhuoban.hm.mvp.pk.presenter.PKPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.widget.DividerDecoration
import java.util.*
import kotlinx.android.synthetic.main.item_pk_object.view.*


class PkMemberFragment : BaseListFragment<PkMember, PKPresenter>(), PKContract.View {

    var mType: String = TYPE_PERSONAL
    var mName: String = ""

    companion object {

        const val TAG = "PkMemberFragment"

        const val TYPE_PERSONAL = "1"
        const val TYPE_TEAM = "2"

        fun newInstance(): PkMemberFragment {
            var fragment = PkMemberFragment()
            var arg = Bundle()
            fragment.arguments = arg
            return fragment
        }

        fun newInstance(type: String): PkMemberFragment {
            var fragment = PkMemberFragment()
            var arg = Bundle()
            arg.putString("Type", type)
            fragment.arguments = arg
            return fragment
        }
    }

    override fun getPresenter(): PKPresenter {
        return PKPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter.getPkMember(mType,mName, page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = requireArguments().getString("Type", TYPE_PERSONAL)

    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerDecoration(
            ContextCompat.getColor(requireContext(), R.color.dy_color_divider),
            DimensionUtils.dp2px(context, 0)
        )
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_pk_object
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PkMember?
    ) {
        viewHolder?.itemView?.tv_name?.text = if (mType == TYPE_TEAM) itemData?.teamName else (if (TextUtils.isEmpty(itemData?.name)) itemData?.username else itemData?.name)
        viewHolder?.itemView?.tv_rank?.text = itemData?.rank
        viewHolder?.itemView?.tv_win_rate?.text = itemData?.rate
        viewHolder?.itemView?.tv_best?.text = itemData?.bestGrade
        viewHolder?.itemView?.tv_join_count?.text = itemData?.num
    }

    override fun onItemClick(data: PkMember?, position: Int) {
        var intent = Intent()
        intent.putExtra("data",data)
        activity?.setResult(Activity.RESULT_OK,intent)
        activity?.finish()
    }

    override fun onLoadPkMember(data: MutableList<PkMember>) {
        super.onLoadPkMember(data)
        loadData(data)
    }

    fun doSearch(name: String) {
        mName = name
        onRequest(DEF_START_PAGE)
    }



}