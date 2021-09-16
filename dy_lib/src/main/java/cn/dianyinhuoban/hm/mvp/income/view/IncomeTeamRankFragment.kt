package cn.dianyinhuoban.hm.mvp.income.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.IncomeDetailBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalRank
import cn.dianyinhuoban.hm.mvp.income.contract.IncomeDetailContract
import cn.dianyinhuoban.hm.mvp.income.presenter.IncomeDetailPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.item_income_team_rank.view.*

class IncomeTeamRankFragment : BaseListFragment<PersonalRank, IncomeDetailPresenter?>(),
    IncomeDetailContract.View {

    companion object {
        fun newInstance(): IncomeTeamRankFragment {
            val args = Bundle()
            val fragment = IncomeTeamRankFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayout(): Int {
        return R.layout.item_income_team_rank
    }

    override fun getContentView(): Int {
        return R.layout.fragment_income_team_rank
    }

    override fun getPresenter(): IncomeDetailPresenter? {
        return IncomeDetailPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchIncomeDetail("2", "", page)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PersonalRank?,
    ) {
        viewHolder?.itemView?.tv_no?.text = (position + 1).toString()
        viewHolder?.itemView?.tv_name?.text = if (itemData == null) {
            "--"
        } else {
            if (!TextUtils.isEmpty(itemData.name)) {
                itemData.name
            } else {
                itemData.username
            }
        }
        viewHolder?.itemView?.tv_amount?.text = if (itemData == null) {
            "--"
        } else {
            NumberUtils.formatMoney(itemData.todayIncome)
        }
    }

    override fun onItemClick(data: PersonalRank?, position: Int) {

    }

    override fun bindIncomeDetail(incomeDetail: IncomeDetailBean?) {
        loadData(incomeDetail?.teamRank)
    }
}