package cn.dianyinhuoban.hm.mvp.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PosterItemBean(
    var description: String?,
    var id: String?,
    var thumb: String?,
    var title: String?,
    var url: String?
):Parcelable