package cn.dianyinhuoban.hm.mvp.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_message.*
import kotlinx.android.synthetic.main.dy_activity_order_list.*
import java.util.*

class OrderListActivity : BaseActivity<IPresenter?>() {

    companion object {

        fun open(context: Context) {
            var intent = Intent(context, OrderListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_order_list)
        setTitle("订单列表")

        var fragments = ArrayList<Fragment>()
        fragments.add(OrderListFragment.newInstance(""))
        fragments.add(OrderListFragment.newInstance("2"))
        fragments.add(OrderListFragment.newInstance("3"))
        fragments.add(OrderListFragment.newInstance("4"))

        tab_order.setViewPager(
            vp_order,
            arrayOf("全部", "待发货", "待确认", "已完成"),
            OrderListActivity@ this,
            fragments
        )

    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}