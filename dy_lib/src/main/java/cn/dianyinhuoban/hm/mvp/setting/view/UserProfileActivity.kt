package cn.dianyinhuoban.hm.mvp.setting.view

import CustomResourceSubscriber
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.UploadResultBean
import cn.dianyinhuoban.hm.mvp.bean.UserBean
import cn.dianyinhuoban.hm.mvp.setting.contract.ProfileContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.ProfilePresenter
import cn.dianyinhuoban.hm.mvp.upload.FileModel
import cn.dianyinhuoban.hm.util.CoilEngine
import coil.load
import coil.transform.CircleCropTransformation
import com.github.gzuliyujiang.wheelpicker.AddressPicker
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity
import com.github.gzuliyujiang.wheelpicker.utility.AddressJsonParser
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider
import kotlinx.android.synthetic.main.dy_activity_user_profile.*
import java.io.File
import java.util.*


class UserProfileActivity : BaseActivity<ProfilePresenter>(), ProfileContract.View {
    private var mDatePicker: DatePicker? = null

    companion object {
        const val REQ_CODE_NICK: Int = 100

        fun open(context: Context) {
            var intent = Intent(context, UserProfileActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_user_profile)
        setTitle("编辑个人资料")

        el_nick.setOnClickListener {
            EditInfoActivity.open(
                UserProfileActivity@ this,
                REQ_CODE_NICK,
                EditInfoActivity.REQ_TYPE_NICK,
                MMKVUtil.getNick()
            )
        }

        img_avatar.setOnClickListener {

            val selectItems = arrayOf("相册", "拍照")

            AlertDialog.Builder(UserProfileActivity@ this)
                .setTitle("选择照片")
                .setItems(
                    selectItems
                ) { _, which ->

                    when (which) {
                        0 -> {

                            PictureSelector
                                .create(UserProfileActivity@ this)
                                .openGallery(PictureMimeType.ofImage())
                                .isEnableCrop(true)
                                .selectionMode(PictureConfig.SINGLE)
                                .cropImageWideHigh(200, 200)
                                .showCropFrame(true)
                                .showCropGrid(false)
                                .imageEngine(CoilEngine.instance)
                                .forResult(object : OnResultCallbackListener<LocalMedia> {
                                    override fun onResult(result: MutableList<LocalMedia>?) {
                                        Log.d("IMAGE", result?.get(0).toString())
                                        img_avatar
                                            .load(File(result?.get(0)?.cutPath)) {
                                                transformations(CircleCropTransformation())
                                            }

                                        val filePath = result?.get(0)?.cutPath
                                        doUploadFile(filePath!!)

                                    }

                                    override fun onCancel() {

                                    }

                                })
                        }

                        1 -> {


                            PictureSelector
                                .create(UserProfileActivity@ this)
                                .openCamera(PictureMimeType.ofImage())
                                .isEnableCrop(true)
                                .selectionMode(PictureConfig.SINGLE)
                                .cropImageWideHigh(200, 200)
                                .imageEngine(CoilEngine.instance)
                                .forResult(object : OnResultCallbackListener<LocalMedia> {
                                    override fun onResult(result: MutableList<LocalMedia>?) {
                                        Log.d("IMAGE", result?.get(0).toString())
                                        img_avatar
                                            .load(File(result?.get(0)?.cutPath)) {
                                                transformations(CircleCropTransformation())
                                            }
                                    }

                                    override fun onCancel() {

                                    }

                                })

                        }
                    }


                }.create().show()


        }

        el_gender.setOnClickListener {

            AlertDialog.Builder(UserProfileActivity@ this)
                .setTitle("选择性别")
                .setItems(
                    arrayOf("男", "女")
                ) { _, which ->
                    tv_gener.text = arrayOf("男", "女")[which]
                    mPresenter.updateProfile("", arrayOf("男", "女")[which], "", "", "")

                }.create().show()
        }

        el_position.setOnClickListener {
            showAddressPicker()
        }

        el_birth.setOnClickListener {
//            TimePickerBuilder(
//                UserProfileActivity@ this
//            ) { date, v ->
//                tv_birth.text = DateTimeUtils.formatDate(date.time, "yyyy年MM月dd日")
//                mPresenter.updateProfile(
//                    "",
//                    "",
//                    "",
//                    DateTimeUtils.formatDate(date.time, "yyyy年MM月dd日"),
//                    ""
//                )
//            }.build().show()
            showTimePicker()
        }

        mPresenter.getProfile()
    }

    override fun getPresenter(): ProfilePresenter? {
        return ProfilePresenter(this)
    }

    override fun onLoadProfile(profile: UserBean) {
        super.onLoadProfile(profile)
        tv_nick.text = profile.name
        tv_gener.text = profile.sex
        tv_birth.text = profile.birthday
        tv_position.text = profile.address
        img_avatar.load(profile.avatar) {
            crossfade(false)
            placeholder(R.drawable.dy_img_avatar_def)
            error(R.drawable.dy_img_avatar_def)
        }
    }

    override fun onUpdateSuccess() {
        super.onUpdateSuccess()

    }

    override fun onUpdateAvatar() {
        super.onUpdateAvatar()
        showToast("更新头像成功")
    }

    @SuppressLint("CheckResult")
    fun doUploadFile(filePath: String) {
        FileModel()
            .upload(File(filePath))
            .compose(SchedulerProvider.getInstance().applySchedulers())
            .compose(ResponseTransformer.handleResult())
            .subscribeWith(object : CustomResourceSubscriber<UploadResultBean?>() {
                override fun onError(exception: ApiException?) {
                    showToast(exception?.displayMessage)
                }


                override fun onNext(t: UploadResultBean) {
                    super.onNext(t)
                    Log.d("UPLOAD", t.url)
                    mPresenter.updateAvatar(t.url)
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_NICK -> {
                    var data = data?.getStringExtra("data")
                    if (!TextUtils.isEmpty(data)) {
                        tv_nick.text = data
                        MMKVUtil.saveNick(data)
                    }
                }
            }
        }
    }

    private fun showTimePicker() {
        if (mDatePicker == null) {
            mDatePicker = DatePicker(this)
            val wheelLayout = mDatePicker?.wheelLayout
            wheelLayout?.setDateMode(DateMode.YEAR_MONTH_DAY)
            wheelLayout?.setDateLabel("年", "月", "日")
            wheelLayout?.setRange(
                DateEntity.target(Calendar.getInstance().get(Calendar.YEAR) - 100, 1, 1),
                DateEntity.today(),
                DateEntity.today()
            )

            mDatePicker?.titleView?.text = "选择日期"
            mDatePicker?.titleView?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_333333
                )
            )
            mDatePicker?.titleView?.textSize = 18f

            mDatePicker?.cancelView?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_999999
                )
            )
            mDatePicker?.cancelView?.textSize = 14f

            mDatePicker?.okView?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_037dff
                )
            )
            mDatePicker?.okView?.textSize = 14f

            mDatePicker?.contentView?.setBackgroundColor(Color.TRANSPARENT)
            mDatePicker?.bodyView?.setBackgroundColor(Color.WHITE)
            mDatePicker?.topLineView?.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.dy_color_divider
                )
            )
            mDatePicker?.headerView?.setBackgroundResource(R.drawable.dy_shape_ffffff_radius_top_6)
            mDatePicker?.setOnDatePickedListener { year, month, day ->
                val monthStr = if (month < 10) {
                    "0${month}"
                } else {
                    month.toString()
                }
                val dayStr = if (day < 10) {
                    "0${day}"
                } else {
                    day.toString()
                }
                val date = "${year}年${monthStr}月${dayStr}日"
                tv_birth.text = date
                mPresenter.updateProfile(
                    "",
                    "",
                    "",
                    date,
                    ""
                )
            }
        }
        if (!mDatePicker?.isShowing!!) {
            mDatePicker?.show()
        }
    }

    private fun showAddressPicker() {
        val picker = AddressPicker(this)
        picker.setAddressMode(
            "city.json", AddressMode.PROVINCE_CITY_COUNTY,
            AddressJsonParser.Builder()
                .provinceCodeField("areaId")
                .provinceNameField("areaName")
                .provinceChildField("cities")
                .cityCodeField("areaId")
                .cityNameField("areaName")
                .cityChildField("counties")
                .countyCodeField("areaId")
                .countyNameField("areaName")
                .build()
        )
        picker.setDefaultValue("贵州省", "毕节地区", "纳雍县")
        picker.setOnAddressPickedListener { province, city, county ->
            updateAddress(province, city, county)
        }
        picker.titleView.text = "选择地区"
        picker.titleView.setTextColor(ContextCompat.getColor(this, R.color.color_333333))
        picker.titleView.textSize = 18f

        picker.cancelView.setTextColor(ContextCompat.getColor(this, R.color.color_999999))
        picker.cancelView.textSize = 14f

        picker.okView.setTextColor(ContextCompat.getColor(this, R.color.color_037dff))
        picker.okView.textSize = 14f

        picker.contentView.setBackgroundColor(Color.TRANSPARENT)
        picker.bodyView.setBackgroundColor(Color.WHITE)
        picker.topLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.dy_color_divider))
        picker.headerView.setBackgroundResource(R.drawable.dy_shape_ffffff_radius_top_6)
        picker.show()
    }

    private fun updateAddress(province: ProvinceEntity?, city: CityEntity?, area: CountyEntity?) {
        tv_position.text = "${province?.name ?: ""}${city?.name ?: ""}${area?.name ?: ""}"
        val addressID = StringBuilder()
        province?.let {
            addressID.append(it.code)
        }
        city?.let {
            if (addressID.isNotEmpty()) {
                addressID.append(",")
            }
            addressID.append(it.code)
        }
        area?.let {
            if (addressID.isNotEmpty()) {
                addressID.append(",")
            }
            addressID.append(it.code)
        }
        mPresenter.updateProfile(
            "",
            "",
            addressID.toString(),
            "",
            ""
        )
    }

}