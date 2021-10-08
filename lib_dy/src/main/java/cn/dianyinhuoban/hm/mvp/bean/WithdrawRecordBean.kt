package cn.dianyinhuoban.hm.mvp.bean

data class WithdrawRecordBean(
    val amount: String,
    val bankName: String,
    val bankNo: String,
    val id: String,
    val inputTime: String,
    val status: String
)