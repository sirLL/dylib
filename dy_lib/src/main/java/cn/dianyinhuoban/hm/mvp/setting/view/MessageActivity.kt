package cn.dianyinhuoban.hm.mvp.setting.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_message.*
import java.util.*

class MessageActivity : BaseActivity<IPresenter?>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_message)
        setTitle("消息通知")

        val fragments = ArrayList<Fragment>()
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_ALL,false))
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_ALL, true))
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_OTHER,false))
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_ANNOUNCE,false))

        tab_message_notify.setViewPager(vp_message_notify, arrayOf("全部","待处理","其他通知","公告") ,MessageActivity@ this, fragments)

        tab_message_notify.showDot(1)

    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}