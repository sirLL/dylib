package cn.dianyinhuoban.hm.mvp.order.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineTypeContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineTypePresenter
import cn.dianyinhuoban.hm.mvp.order.OrderListActivity
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_product_list.*

class ProductListActivity : BaseActivity<MachineTypePresenter?>(), MachineTypeContract.View {
    override fun getPresenter(): MachineTypePresenter? {
        return MachineTypePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("机具采购")
        setRightButtonText("采购订单") {
            startActivity(Intent(ProductListActivity@ this, OrderListActivity::class.java))
        }
        setContentView(R.layout.dy_activity_product_list)
        mPresenter?.fetchMachineType()
    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {
        val titles: ArrayList<String> = arrayListOf<String>()
        val fragmentList = arrayListOf<Fragment>()
        data?.let {
            if (it.isNotEmpty()) {
                for (bean in it) {
                    bean?.let {
                        if (!TextUtils.isEmpty(bean.name) && !TextUtils.isEmpty(bean.id)) {
                            titles.add(bean.name)
                            fragmentList.add(ProductListFragment.newInstance(bean.id))
                        }
                    }
                }
            }
        }
        if (titles.size > 0) {
            val titleArr = arrayOfNulls<String>(titles.size)
            for (i in 0 until titles.size) {
                titleArr[i] = titles[i]
            }
            tab_layout.setViewPager(
                view_pager,
                titleArr, this, fragmentList
            )
        }

    }
}