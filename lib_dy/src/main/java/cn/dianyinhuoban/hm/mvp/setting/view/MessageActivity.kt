package cn.dianyinhuoban.hm.mvp.setting.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.NotifyMessageBean
import cn.dianyinhuoban.hm.mvp.setting.contract.MessageContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.MessagePresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_message.*
import java.util.*

class MessageActivity : BaseActivity<MessagePresenter?>(), MessageContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_message)
        setTitle("消息通知")

        val fragments = ArrayList<Fragment>()
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_ALL, false))
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_PK, true))
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_OTHER, false))
        fragments.add(MessageNotifyFragment.newInstance(MessageNotifyFragment.TYPE_ANNOUNCE, false))

        tab_message_notify.setViewPager(
            vp_message_notify,
            arrayOf("全部", "待处理", "其他通知", "公告"),
            MessageActivity@ this,
            fragments
        )


    }

    override fun getPresenter(): MessagePresenter? {
        return MessagePresenter(this)
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.getMessageList(1, MessageNotifyFragment.TYPE_PK, "1")
    }

    override fun onLoadMessageList(data: MutableList<NotifyMessageBean>) {
        if (data.size > 0) {
            tab_message_notify.showDot(1)
        } else {
            tab_message_notify.hideMsg(1)
        }
    }

    override fun onLoadMessageDetail(data: NotifyMessageBean) {
        super.onLoadMessageDetail(data)
    }
}