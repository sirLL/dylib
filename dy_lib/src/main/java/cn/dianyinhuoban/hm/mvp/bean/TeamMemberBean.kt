package cn.dianyinhuoban.hm.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")// 用于处理 Lint 的错误提示
@Parcelize
data class TeamMemberBean(
    val avatar: String?,
    val name: String?,
    val personal: String?,
    val uid: String?,
    val username: String?,
    val inviteNum: String?,
    val personalTrans: String?,
    val machineTotal: String?,
    val machineActive: String?,
    val transTotal: String?,
    val nonActive: String?
): Parcelable