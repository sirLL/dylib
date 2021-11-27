package cn.dianyinhuoban.hm.mvp.setting.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.WebActivity
import cn.dianyinhuoban.hm.mvp.bean.JiangWuTangBean
import cn.dianyinhuoban.hm.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.SettingPresenter
import coil.load
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils

class JiangWuTangFragment : BaseListFragment<JiangWuTangBean, SettingPresenter>(),
    SettingContract.View {
    companion object {
        fun newInstance(): JiangWuTangFragment {
            val args = Bundle()
            val fragment = JiangWuTangFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): SettingPresenter {
        return SettingPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter.getJiangWuTangList(page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_jwt
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: JiangWuTangBean?
    ) {
        viewHolder?.getView<ImageView>(R.id.iv_cover)?.load(itemData?.thumb) {
            crossfade(true)
        }
        viewHolder?.setText(R.id.tv_title, itemData?.title ?: "")
        viewHolder?.setText(
            R.id.tv_date, if (itemData?.inputtime.isNullOrBlank()) {
                ""
            } else {
                DateTimeUtils.formatDate(
                    itemData!!.inputtime.toLong() * 1000,
                    DateTimeUtils.PATTERN_YYYY_MM_DD
                )
            }
        )
    }

    override fun onItemClick(data: JiangWuTangBean?, position: Int) {
        data?.let {
            WebActivity.openWebActivity(requireContext(), it.title, it.url)
        }
    }

    override fun onLoadJWTList(data: MutableList<JiangWuTangBean>) {
        super.onLoadJWTList(data)
        loadData(data)
    }

}