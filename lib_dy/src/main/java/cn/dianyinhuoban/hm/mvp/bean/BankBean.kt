package cn.dianyinhuoban.hm.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@SuppressLint("ParcelCreator")
@Parcelize
data class BankBean(
    val bankName: String?,
    val bankNo: String?,
    val id: String?,
    val inputTime: String?,
    val isDefault: Int?,
    val name: String?,
    val phone: String?,
    val uid: Int?
): Parcelable {}