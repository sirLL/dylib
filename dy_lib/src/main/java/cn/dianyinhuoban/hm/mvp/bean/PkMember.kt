package cn.dianyinhuoban.hm.mvp.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PkMember (
    var uid: String,
    var name: String?,
    var username: String,
    var teamName: String?,
    var rate: String?,
    var num: String?,
    var bestGrade: String?,
    var rank: String?

): Parcelable