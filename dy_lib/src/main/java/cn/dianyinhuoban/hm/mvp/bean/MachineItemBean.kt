package cn.dianyinhuoban.hm.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MachineItemBean(
    val act_status: String,
    val act_time: String,
    val id: String,
    val isNew: String,
    val member_id: String,
    val name: String,
    val pos_sn: String
):Parcelable
