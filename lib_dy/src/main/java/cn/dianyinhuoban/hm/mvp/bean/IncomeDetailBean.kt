package cn.dianyinhuoban.hm.mvp.bean

data class IncomeDetailBean(
    var current: String?,
    var detailList: List<DetailBean>?,
    var rate: String?,
    var subLeft: String?,
    var subRight: String?,
    var teamRank: List<PersonalRank>?
)

data class PersonalRank(
    var name: String?,
    var todayIncome: String?,
    var username: String?,
    var avatar: String?
)

data class DetailBean(
    var id: String?,
    var price: String?,
    var add_time: String?,
    var order_no: String?,
    var customerName: String?,
    var type:String?,
    var note: String?
)



