package cn.dianyinhuoban.hm.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PurchaseProductBean(
    var describe: String?,
    var id: String?,
    var img: String?,
    var name: String?,
    var price: String?,
    var set_meal: String?,
    var typeName: String?
) : Parcelable