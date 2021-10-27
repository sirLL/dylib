package cn.dianyinhuoban.hm.mvp.setting.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.NotifyMessageBean
import cn.dianyinhuoban.hm.mvp.order.OrderDetailActivity
import cn.dianyinhuoban.hm.mvp.pk.contract.PKContract
import cn.dianyinhuoban.hm.mvp.pk.presenter.PKPresenter
import cn.dianyinhuoban.hm.mvp.setting.contract.MessageContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.MessagePresenter
import cn.dianyinhuoban.hm.util.StringUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.wareroom.lib_base.ui.BaseFragment
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.widget.DividerDecoration
import kotlinx.android.synthetic.main.dy_fragment_message_notify.*
import kotlinx.android.synthetic.main.dy_item_message_active.view.*
import kotlinx.android.synthetic.main.dy_item_message_dispatch.view.*
import kotlinx.android.synthetic.main.dy_item_message_pk.view.*

class MessageNotifyFragment : BaseFragment<MessagePresenter>(), MessageContract.View,
    PKContract.View {

    var mPage: Int = 1
    var mType: String = TYPE_ALL
    var mIsFresh: String = "1"
    var mIsRefreshData: Boolean = false

    val mPkPresenter: PKPresenter by lazy {
        PKPresenter(this)
    }


    companion object {

        const val TYPE_ALL = ""
        const val TYPE_ANNOUNCE = "1"
        const val TYPE_PK = "2"
        const val TYPE_OTHER = "3"

        const val STATUS_OK = "2"
        const val STATUS_REUSE = "3"

        fun newInstance(type: String, isFresh: Boolean): MessageNotifyFragment {
            var fragment = MessageNotifyFragment()
            var args = Bundle()
            args.putString("type", type)
            args.putBoolean("isFresh", isFresh)
            fragment.arguments = args

            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mType = arguments?.getString("type", TYPE_ALL).toString()
        mIsFresh = if (arguments?.getBoolean("isFresh", false)!!) "1" else "2"
    }

    override fun getPresenter(): MessagePresenter {
        return MessagePresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_message_notify
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_layout.setRefreshHeader(ClassicsHeader(activity))
        refresh_layout.setRefreshFooter(ClassicsFooter(activity))
        refresh_layout.setEnableRefresh(true)

        refresh_layout.setOnRefreshListener {
            mPage = 1
            doRequest()

        }

        refresh_layout.setOnLoadMoreListener {
            mPage++
            doRequest()
        }

        recycler_view.adapter = NotifyMessageAdapter(mutableListOf())
        recycler_view.addItemDecoration(
            DividerDecoration(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dy_base_color_transparent
                ), DimensionUtils.dp2px(context, 12)
            )
        )


        doRequest()
    }

    private fun doRequest() {
        mIsRefreshData = (mPage == 1)
        mPresenter.getMessageList(mPage, mType, mIsFresh)
    }

    //1,公告 2.pk通知 3.其他通知
    inner class NotifyMessageAdapter(data: MutableList<NotifyMessageBean>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var dataList = data

        //1 公告 2 pk通知 3 激活 4 发货 5提现通过 6 提现拒绝 7 新增下级
//        private val VIEW_ITEM_ANNOUNCE = 1
//        private val VIEW_ITEM_PK = 2
//        private val VIEW_ITEM_OTHER = 3
//        private val VIEW_ITEM_DISPATCH = 4;
//        private val VIEW_ITEM_WITHDRAWAL_SUCCESS = 5
//        private val VIEW_ITEM_WITHDRAWAL_FAIL = 6
//        private val VIEW_ITEM_ADD_SUB = 7

        private val VIEW_ITEM_MESSAGE = 10
        private val VIEW_ITEM_DISPATCH = 11
        private val VIEW_ITEM_PK = 12

        fun setData(data: MutableList<NotifyMessageBean>) {
            dataList = data
            notifyDataSetChanged()
        }

        fun appendData(data: MutableList<NotifyMessageBean>) {
            dataList.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_ITEM_DISPATCH -> DispatchViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.dy_item_message_dispatch, parent, false)
                )
                VIEW_ITEM_PK -> PkNotifyViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.dy_item_message_pk, parent, false)
                )
                else -> OtherViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.dy_item_message_active, parent, false)
                )
            }
        }

        //1 公告 2 pk通知 3 激活 4 发货 5提现通过 6 提现拒绝 7 新增下级
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val data = dataList[position]
            when (holder.itemViewType) {
                VIEW_ITEM_MESSAGE -> {
                    val h = (holder as OtherViewHolder)
                    h.itemView.tv_msg_title.text = data.title
                    h.itemView.tv_msg_sub_title.text = data.subTitle
                    h.itemView.tv_msg_content.text = data.content
                    h.itemView.tv_msg_time.text =
                        DateTimeUtils.getYYYYMMDDHHMMSS((data.inputTime.toLong() * 1000))

                    h.itemView.tv_msg_sub_title.visibility = if (data.subTitle.isNullOrBlank()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
                    when (data.type) {
                        "2", "11", "12", "15" -> {
                            h.itemView.img_msg_logo.setImageResource(R.drawable.dy_ic_msg_invite)
                        }
                        "3", "5", "6" -> {
                            h.itemView.img_msg_logo.setImageResource(R.drawable.dy_ic_msg_active)
                        }
                        "4" -> {
                            h.itemView.img_msg_logo.setImageResource(R.drawable.dy_ic_msg_ship)
                        }
                        "7" -> {
                            h.itemView.img_msg_logo.setImageResource(R.drawable.dy_ic_msg_add_child)
                        }
                        else -> {
                            h.itemView.img_msg_logo.setImageResource(R.drawable.dy_ic_msg_activity)
                        }
                    }
                }

                VIEW_ITEM_DISPATCH -> {
                    val h = (holder as DispatchViewHolder)
                    h.itemView.img_msg_dispatch_logo.setImageResource(R.drawable.dy_ic_msg_ship)
                    h.itemView.tv_msg_dispatch_title.text = data.title
                    h.itemView.tv_msg_dispatch_sub_title.text = data.subTitle
                    h.itemView.tv_msg_dispatch_content.text = data.content
                    h.itemView.tv_msg_dispatch_time.text =
                        DateTimeUtils.getYYYYMMDDHHMMSS((data.inputTime.toLong() * 1000))

                    h.itemView.tv_msg_dispatch.setOnClickListener {
                        var content = ""
                        if (!data.content.isNullOrBlank() && data.content.contains("运单号:")) {
                            val strList = data.content.split("运单号:")
                            if (strList.size >= 2) {
                                content = strList[1]
                            }
                        }
                        StringUtil.copyString(requireContext(), content)
                        showToast("复制成功")
                    }
                    h.itemView.tv_msg_dispatch_refuse.setOnClickListener {
                        OrderDetailActivity.open(requireContext(), data.cid)
                    }
                }

                VIEW_ITEM_PK -> {
                    val h = (holder as PkNotifyViewHolder)
                    h.itemView.img_msg_pk_logo.setImageResource(R.drawable.dy_ic_msg_invite)
                    h.itemView.tv_msg_pk_title.text = data.title
                    h.itemView.tv_msg_pk_content.text = data.subTitle
                    h.itemView.tv_msg_pk_time.text =
                        DateTimeUtils.getYYYYMMDDHHMMSS((data.inputTime.toLong() * 1000).toLong())

                    h.itemView.tv_msg_pk_refuse.setOnClickListener {
                        mPkPresenter.responsePK(STATUS_REUSE, data.id, data.cid)
                    }

                    h.itemView.tv_msg_pk.setOnClickListener {
                        mPkPresenter.responsePK(STATUS_OK, data.id, data.cid)
                    }
                }


            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        //1 公告 2 pk通知 3 激活 4 发货 5提现通过 6 提现拒绝 7 新增下级
        override fun getItemViewType(position: Int): Int {
//            val notifyMessageBean = dataList[position]
//            if (notifyMessageBean.type.toInt() == 1
//                || notifyMessageBean.type.toInt() == 3
//                || notifyMessageBean.type.toInt() == 7
//                || notifyMessageBean.type.toInt() == 6
//                || notifyMessageBean.type.toInt() == 5
//                || (notifyMessageBean.type.toInt() == 2 && notifyMessageBean.isDeal.toInt() == 2)
//            ) {
//                return VIEW_ITEM_MESSAGE
//            } else if (notifyMessageBean.type.toInt() == 4) {
//                return VIEW_ITEM_DISPATCH
//            } else {
//                return VIEW_ITEM_PK
//            }
            val notifyMessageBean = dataList[position]
            return if (notifyMessageBean.type.toInt() == 2 && notifyMessageBean.isDeal.toInt() == 1) {
                //PK 未处理
                VIEW_ITEM_PK
            } else if (notifyMessageBean.type.toInt() == 4) {
                //发货
                VIEW_ITEM_DISPATCH
            } else {
                VIEW_ITEM_MESSAGE
            }
        }


        inner class DispatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        inner class PkNotifyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }

        inner class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        }
    }

    override fun onLoadMessageList(data: MutableList<NotifyMessageBean>) {
        refresh_layout.finishRefresh()
        refresh_layout.finishLoadMore()
        if (mIsRefreshData) {
            (recycler_view.adapter as NotifyMessageAdapter).setData(data)
        } else {
            (recycler_view.adapter as NotifyMessageAdapter).appendData(data)
        }
    }

    override fun onLoadMessageDetail(data: NotifyMessageBean) {

    }

    override fun onResponsePKSuccess() {
        super.onResponsePKSuccess()
        showMessage("操作成功")
    }

}