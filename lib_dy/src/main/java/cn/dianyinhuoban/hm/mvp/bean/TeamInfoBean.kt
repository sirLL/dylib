package cn.dianyinhuoban.hm.mvp.bean

data class TeamInfoBean(
    var auth: String?,
    var list: List<MemberBean>?,
    var nonAuth: String?,
    var total: String?
)

data class MemberBean(
    var avatar: String?,
    var name: String?,
    var rate: String?,
    var regtime: String?,
    var teamMonth: String?,
    var uid: String?
)