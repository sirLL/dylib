package cn.dianyinhuoban.hm.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class AddressBean(
    var address: String?,
    var area: String?,
    var cityId: String?,
    var districtId: String?,
    var id: String?,
    var inputTime: String?,
    var isDefault: String?,
    var name: String?,
    var phone: String?,
    var provinceId: String?,
    var uid: String?
) : Parcelable