package cn.dianyinhuoban.hm.mvp.bean

data class MyMachineBean(
    val activeCount: String,
    val count: String,
    val `data`: List<MachineItemBean>,
    val nonActiveCount: String
)

