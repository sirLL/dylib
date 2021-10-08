package cn.dianyinhuoban.hm.mvp.setting.view

import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.AuthResult
import cn.dianyinhuoban.hm.mvp.bean.JiangWuTangBean
import cn.dianyinhuoban.hm.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.SettingPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_jwt.view.*

class JiangWuTangFragment : BaseListFragment<JiangWuTangBean, SettingPresenter>(), SettingContract.View {

    override fun getPresenter(): SettingPresenter {
        return SettingPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter.getJiangWuTangList(page)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_jwt
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
        viewHolder?.itemView?.tv_title?.text = itemData?.title

    }

    override fun onItemClick(data: JiangWuTangBean?, position: Int) {

    }

    override fun onLoadJWTList(data: MutableList<JiangWuTangBean>) {
        super.onLoadJWTList(data)
        loadData(data)

    }

}