package cn.dianyinhuoban.hm.mvp.poster.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.PosterItemBean
import cn.dianyinhuoban.hm.mvp.poster.contract.PosterListContract
import cn.dianyinhuoban.hm.mvp.poster.presenter.PosterListPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import kotlinx.android.synthetic.main.dy_item_poster.view.*

class PosterListFragment : BaseListFragment<PosterItemBean, PosterListPresenter?>(),
    PosterListContract.View {
    val mDp2px16: Int by lazy {
        DimensionUtils.dp2px(requireContext(), 16)
    }
    val mDp2px6: Float by lazy {
        DimensionUtils.dp2px(requireContext(), 6).toFloat()
    }
    val mDp2px10: Int by lazy {
        DimensionUtils.dp2px(requireContext(), 10)
    }
    private var mTypeID: String? = null

    companion object {
        fun newInstance(typeID: String?): PosterListFragment {
            val args = Bundle()
            args.putString("typeID", typeID)
            val fragment = PosterListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): PosterListPresenter? {
        return PosterListPresenter(this)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_poster
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTypeID = arguments?.getString("typeID", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView.setPadding(mDp2px16, 0, mDp2px16, 0)
        mPresenter?.fetchPosterList(mTypeID ?: "", DEF_START_PAGE)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(
            requireContext(), 2,
            RecyclerView.VERTICAL, false
        )
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return GridSpacingItemDecoration(2, mDp2px10, false)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchPosterList(mTypeID ?: "", page)
    }

    override fun bindPosterList(data: List<PosterItemBean>) {
        loadData(data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PosterItemBean?
    ) {
        viewHolder?.itemView?.iv_cover?.load(itemData?.thumb ?: "") {
            crossfade(true)
            transformations(RoundedCornersTransformation(mDp2px6, mDp2px6, mDp2px6, mDp2px6))
        }

        viewHolder?.itemView?.tv_share?.setOnClickListener {
            itemData?.let {
                PosterEditActivity.openPosterEditActivity(requireContext(), it)
            }
        }
    }

    override fun onItemClick(data: PosterItemBean?, position: Int) {

    }
}