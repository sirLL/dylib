package cn.dianyinhuoban.hm.mvp.bean

import java.math.BigDecimal

data class PkDataBean(
    var equalData: EqualData?,
    var myData: MyData,
    var teamList: MutableList<TeamData>?
)

data class EqualData(


    val active: String,
    val endTime: String?,
    val name: String,
    val rank: String?,
    val recently7days: Recently7days,
    val startTime: String?,
    //1.发起、2.接受、3.拒绝、4.完成
    val status: String,
    val subUids: String?,
    val teamName: String?,
    val username: String,
    val yesterday: Yesterday
) {

    companion object {
        const val STATUS_LAUNCH: String = "1"
        const val STATUS_RECEIVE: String = "2"
        const val STATUS_REFUSE: String = "3"
        const val STATUS_COMPLETE: String = "4"
    }
}


data class MyData(
    var active: String,
    var name: String,
    var rank: String?,
    var recently7days: Recently7days,
    var subUids: String?,
    var teamName: String?,
    var username: String,
    var yesterday: Yesterday
)

data class Recently7days(
    var active: String,
    var posTrans: String
)

data class Yesterday(
    var active: String,
    var posTrans: String
)

data class TeamData(
    var uid: String,
    var name: String,
    var username: String,
    var yesterdayActive: String,
    var recent7DayActive: String,
    var yesterdayPos: String,
    var recently7daysPos: String,
)


