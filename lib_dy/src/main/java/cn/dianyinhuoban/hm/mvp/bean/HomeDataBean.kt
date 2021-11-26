package cn.dianyinhuoban.hm.mvp.bean

data class HomeDataBean(
    val newNotice: Int,
    val personal: Personal,
    val personalPK: PK,
    val personalRank: Rank,
    val team: Team,
    val teamPK: PK,
    val teamRank: Rank,
    val userInfo: UserInfo
)

data class PK(
    val rank: String,
    val activeTrans: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val status: String
)

data class Rank(
    val activeTrans: String,
    val id: String,
    val inputTime: String,
    val machineTrans: String,
    val rank: String,
    val total: String,
    val type: String,
    val uid: String
)

data class UserInfo(
    val name: String,
    val teamName: String,
    val uid: String,
    val username: String
)

data class WeekMonth(
    val id: String,
    val uid: String,
    val rank: String,
    val total: String,
    val machineTrans: String,
    val activeTrans: String,
    val type: String,
    val inCome: String,
    val activeMachine: String,
    val inputTime: String
)

data class Personal(
    val day: WeekMonth,
    val month: WeekMonth
)

data class Team(
    val day: WeekMonth,
    val month: WeekMonth
)

