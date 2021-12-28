package cn.dianyinhuoban.hm.mvp.bean

data class NotifyMessageBean(
    val cid: String,
    val content: String,
    val id: String,
    val inputTime: String,
    val isDeal: String,
    val isRead: String,
    val subTitle: String,
    val title: String,
    val type: String,
    val uid: String,
    val url: String

    //通知类型 1 公告 2 pk通知 3 激活 4 发货 5提现通过 6 提现拒绝 7 新增下级
)