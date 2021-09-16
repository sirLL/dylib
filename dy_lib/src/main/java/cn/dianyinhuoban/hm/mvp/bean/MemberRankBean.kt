package cn.dianyinhuoban.hm.mvp.bean

data class MemberRankBean(
    var list: List<MemberRank>?,
    var myRank: MemberMyRank?
)

data class MemberRank(
    var avatar: String?,
    var name: String?,
    var personal: String?,
    var rank: String?,
    var uid: String?,
    var username: String?
)

data class MemberMyRank(
    var personal: String?,
    var rank: String?
)