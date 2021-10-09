package cn.dianyinhuoban.hm.mvp.me.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MeMenuBean
import cn.dianyinhuoban.hm.mvp.bean.PersonalBean
import cn.dianyinhuoban.hm.mvp.income.view.IncomeActivity
import cn.dianyinhuoban.hm.mvp.income.view.WithdrawActivity
import cn.dianyinhuoban.hm.mvp.machine.view.MachineTransferActivity
import cn.dianyinhuoban.hm.mvp.me.contract.MeContract
import cn.dianyinhuoban.hm.mvp.me.presenter.MePresenter
import cn.dianyinhuoban.hm.mvp.me.view.adapter.MeMenuAdapter
import cn.dianyinhuoban.hm.mvp.me.view.adapter.MeMenuAdapter.OnMenuClickListener
import cn.dianyinhuoban.hm.mvp.order.OrderListActivity
import cn.dianyinhuoban.hm.mvp.order.view.ProductListActivity
import cn.dianyinhuoban.hm.mvp.pk.view.PkActivity
import cn.dianyinhuoban.hm.mvp.setting.view.*
import cn.dianyinhuoban.hm.widget.dialog.MessageDialog
import coil.load
import coil.transform.CircleCropTransformation
import com.wareroom.lib_base.ui.BaseFragment
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.OSUtils
import kotlinx.android.synthetic.main.dy_fragment_me.*
import java.util.*


class MeFragment : BaseFragment<MePresenter?>(), MeContract.View {
    private var mAdapter: MeMenuAdapter? = null

    companion object {
        fun newInstance(): MeFragment {
            val args = Bundle()
            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): MePresenter? {
        return MePresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_me
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadMenuData()

        //收益
        view_income_action.setOnClickListener {
            startActivity(Intent(context, IncomeActivity::class.java))
        }
        //提现
        tv_withdraw.setOnClickListener {
            startActivity(Intent(context, WithdrawActivity::class.java))
        }
        //我的客户
        cl_my_client.setOnClickListener {
            startActivity(Intent(context, MyClientActivity::class.java))
        }
        //我的成员
        cl_member.setOnClickListener {
            startActivity(Intent(context, MemberActivity::class.java))
        }

        iv_message.setOnClickListener {
            startActivity(Intent(context, MessageActivity::class.java))
        }
        cl_my_parent.setOnClickListener {
            val name = tv_parent_name.text
            val phone = tv_parent_phone.text.toString()
            if (!TextUtils.isEmpty(phone)) {
                MessageDialog(requireContext())
                    .setMessage("是否拨打${name}电话?")
                    .setOnConfirmClickListener {
                        it.dismiss()
                        OSUtils.callPhone(requireContext(), phone)
                    }.show()
            }

        }
    }

    private fun setupRecyclerView() {
        mAdapter = MeMenuAdapter()
        recycler_view.adapter = mAdapter
        mAdapter?.onMenuClickListener = object : OnMenuClickListener {
            override fun onMenuClick(menuBean: MeMenuBean) {
                when (menuBean.id) {
                    1 -> {//机具划拨
                        startActivity(Intent(context, MachineTransferActivity::class.java))
                    }
                    2 -> {//机具采购
                        startActivity(Intent(context, ProductListActivity::class.java))
                    }
                    3 -> {//讲武堂
                        startActivity(Intent(context, JiangWuTangActivity::class.java))
                    }
                    4 -> {//采购订单
                        startActivity(Intent(context, OrderListActivity::class.java))
                    }
                    5 -> {//银行卡
                        startActivity(Intent(context, BankManageActivity::class.java))
                    }
                    6 -> {//PK
                        startActivity(Intent(context, PkActivity::class.java))
                    }
                    7 -> {//授权书
                        startActivity(Intent(context, AuthActivity::class.java))
                    }
                    8 -> {//在线客服

                    }
                    9 -> {//设置
                        startActivity(Intent(context, SettingActivity::class.java))
                    }
                }
            }
        }
    }

    private fun loadMenuData() {
        val menuData: MutableList<MeMenuBean> = ArrayList()
        menuData.add(MeMenuBean(1, "机具划拨", R.drawable.dy_ic_me_menu_transfer))
        menuData.add(MeMenuBean(2, "机具采购", R.drawable.dy_ic_me_menu_purchase))
        menuData.add(MeMenuBean(3, "讲武堂", R.drawable.dy_ic_me_menu_school))
        menuData.add(MeMenuBean(4, "采购订单", R.drawable.dy_ic_me_menu_purchase_order))
        menuData.add(MeMenuBean(5, "银行卡", R.drawable.dy_ic_me_menu_bank_card))
        menuData.add(MeMenuBean(6, "PK", R.drawable.dy_ic_me_menu_pk))
        menuData.add(MeMenuBean(7, "授权书", R.drawable.dy_ic_me_menu_auth))
//        menuData.add(MeMenuBean(8, "在线客服", R.drawable.ic_me_menu_online_service))
        menuData.add(MeMenuBean(9, "设置", R.drawable.dy_ic_me_menu_setting))
        mAdapter?.data = menuData
    }

    override fun onStart() {
        super.onStart()
        fetchPersonalData()
    }

    private fun fetchPersonalData() {
        mPresenter?.fetchPersonalData()
    }

    override fun bindPersonalData(personalBean: PersonalBean?) {
        personalBean?.let {
            //头像
            iv_avatar.load(it.avatar) {
                crossfade(true)//淡入效果
                allowHardware(false)
                placeholder(R.drawable.dy_img_avatar_def)
                error(R.drawable.dy_img_avatar_def)
                transformations(CircleCropTransformation())
            }
            //昵称
            tv_name.text = if (TextUtils.isEmpty(it.name)) {
                it.username
            } else {
                it.name
            }
            //等级
            tv_level.text = it.title
            //账户总额
            tv_amount.text = NumberUtils.numberScale(it.total)
            //个人收益
            tv_amount_personal.text = NumberUtils.formatMoney(it.personal)
            //团队收益
            tv_amount_team.text = NumberUtils.formatMoney(it.team)
            //激活返现
            tv_amount_activation.text = NumberUtils.formatMoney(it.personalActive)
            //采购奖励
            tv_amount_purchase.text = NumberUtils.formatMoney(it.purchase)
            //团队名称
            tv_team_name.text = if (TextUtils.isEmpty(it.teamName)) {
                "${it.username})的团队"
            } else {
                it.teamName
            }
            tv_integral.text = NumberUtils.formatMoney(it.point)


            tv_parent_name.text = it.parentName ?: ""
            tv_parent_phone.text = it.parentPhone ?: ""
        }
        cl_my_parent.visibility = if (TextUtils.isEmpty(personalBean?.parentPhone)) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}