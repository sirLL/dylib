package cn.dianyinhuoban.hm.mvp.bean

data class MemberDetailBean(
    var avatar: String?,
    var inviteNum: String?,
    var machineActive: String?,
    var machineTotal: String?,
    var monthTrans: String?,
    var name: String?,
    var purchaseList: List<Purchase>?,
    var purchaseTotal: String?,
    var teamRank: String?,
    var uid: String?,
    var username: String?,
    var weekTrans: String?,
    var monthlyTrans: String?,
    var dailyTrans: String?
)

data class Purchase(
    var inputTime: String?,
    var num: String?,
)