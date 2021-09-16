package cn.dianyinhuoban.hm.mvp.income.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.BankBean
import cn.dianyinhuoban.hm.mvp.income.contract.BankCardListContract
import cn.dianyinhuoban.hm.mvp.income.presenter.BankCardListPresenter
import cn.dianyinhuoban.hm.mvp.me.view.BindBankCardActivity
import cn.dianyinhuoban.hm.util.StringUtil
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.base_bottom_picker.*
import kotlinx.android.synthetic.main.fragment_withdraw_type_picker.*
import kotlinx.android.synthetic.main.fragment_withdraw_type_picker.recycler_view
import kotlinx.android.synthetic.main.item_withdraw_type.view.*

class WithdrawTypePicker : BaseBottomPicker<BankBean, BankCardListPresenter>(),
    BankCardListContract.View {
    companion object {
        fun newInstance(): WithdrawTypePicker {
            val args = Bundle()
            val fragment = WithdrawTypePicker()
            fragment.arguments = args
            return fragment
        }
    }

    private var mCheckedID: String? = null

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getTitle(): String {
        return "提现方式"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request(DEF_START_PAGE)
    }

    fun setCheckedID(checkedID: String?) {
        if (TextUtils.isEmpty(mCheckedID)) {
            mCheckedID = checkedID
        }
    }

    override fun setupRecyclerView() {
        mAdapter = object : SimpleAdapter<BankBean>(getItemLayoutRes()) {
            override fun convert(
                viewHolder: SimpleViewHolder?,
                position: Int,
                itemData: BankBean?
            ) {
                if (mData == null || mData.size == position) {
                    viewHolder?.itemView?.iv_hook?.visibility = View.INVISIBLE
                    viewHolder?.itemView?.iv_icon?.setImageResource(R.drawable.ic_bank_card_add_picker)
                    viewHolder?.itemView?.tv_title?.text = "使用新卡提现"
                } else {
                    this@WithdrawTypePicker.convert(viewHolder, position, itemData)
                }
            }

            override fun onItemClick(data: BankBean?, position: Int) {
                if (mData == null || mData.size == position) {
                    //添加银行卡
                    startActivity(Intent(context, BindBankCardActivity::class.java))
                    dismiss()
                } else {
                    mCheckedID = data?.id
                    mAdapter?.notifyItemChanged(position)
                    this@WithdrawTypePicker.onItemClick(data, position)
                    dismiss()
                }
            }

            override fun getItemCount(): Int {
                return if (mData == null) {
                    1
                } else {
                    mData.size + 1
                }
            }
        }
        recycler_view.adapter = mAdapter
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_withdraw_type
    }

    override fun getPresenter(): BankCardListPresenter? {
        return BankCardListPresenter(this)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: BankBean?
    ) {
        viewHolder?.itemView?.iv_hook?.visibility =
            if (!TextUtils.isEmpty(mCheckedID) && mCheckedID == itemData?.id) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        viewHolder?.itemView?.iv_icon?.setImageResource(R.drawable.ic_bank_card_picker)
        viewHolder?.itemView?.tv_title?.text =
            "${itemData?.bankName}(${StringUtil.getBankCardEndNo(itemData?.bankNo)})"
    }

    override fun request(page: Int) {
        mPresenter?.getBankList()
    }

    override fun onLoadBankList(bankBeanList: List<BankBean>) {
        loadData(bankBeanList)
    }


}