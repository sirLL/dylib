package cn.dianyinhuoban.hm.mvp.setting.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.setting.contract.AddShipAddressContract
import cn.dianyinhuoban.hm.mvp.setting.presenter.AddShipAddressPresenter
import cn.dianyinhuoban.hm.widget.dialog.MessageDialog
import com.github.gzuliyujiang.wheelpicker.AddressPicker
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity
import com.github.gzuliyujiang.wheelpicker.utility.AddressJsonParser
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import kotlinx.android.synthetic.main.dy_activity_add_ship_address.*


class AddShipAddressActivity : BaseActivity<AddShipAddressPresenter?>(),
    AddShipAddressContract.View {

    private var mAddress: AddressBean? = null
    private var mProvince: ProvinceEntity? = null
    private var mCity: CityEntity? = null
    private var mDistrict: CountyEntity? = null

    companion object {
        fun openAddShipAddressActivity(
            activity: Activity,
            address: AddressBean?,
            requestCode: Int
        ) {
            val intent = Intent(activity, AddShipAddressActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("address", address)
            intent.putExtras(bundle)
            activity.startActivityForResult(intent, requestCode)
        }

        fun openAddShipAddressActivity(
            fragment: Fragment,
            address: AddressBean?,
            requestCode: Int
        ) {
            val intent = Intent(fragment.requireContext(), AddShipAddressActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("address", address)
            intent.putExtras(bundle)
            fragment.startActivityForResult(intent, requestCode)
        }


    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mAddress = bundle?.getParcelable("address")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_add_ship_address)
        setTitle("添加收货地址")
        setupAction()
        setupInputListener()
        loadAddress()
        mAddress?.let {
            setRightButtonText("删除") {
                showDeleteDialog()
            }
        }
    }

    private fun setupInputListener() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                setupSubmitEnable()
            }

        }
        ed_name.addTextChangedListener(watcher)
        ed_phone.addTextChangedListener(watcher)
        tv_city.addTextChangedListener(watcher)
        ed_address.addTextChangedListener(watcher)
    }

    private fun setupSubmitEnable() {
        val name = ed_name.text.toString()
        val phone = ed_phone.text.toString()
        val city = tv_city.text.toString()
        val address = ed_address.text.toString()
        btn_submit.isEnabled =
            name.length >= 2
                    && ValidatorUtils.isMobile(phone)
                    && city.length >= 2
                    && address.length >= 4
    }

    private fun setupAction() {
        tv_city.setOnClickListener {
            showAddressPicker()
        }
        btn_submit.setOnClickListener {
            submitAddress()
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
            bindCheckedCity(province, city, county)
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

    private fun bindCheckedCity(province: ProvinceEntity?, city: CityEntity?, area: CountyEntity?) {
        mProvince = province
        mCity = city
        mDistrict = area
        tv_city.text =
            "${mProvince?.name ?: ""}${mCity?.name ?: ""}${mDistrict?.name ?: ""}"
        setupSubmitEnable()
    }

    private fun loadAddress() {
        val name = mAddress?.name ?: ""
        val phone = mAddress?.phone ?: ""
        val address = mAddress?.address ?: ""
        ed_name.setText(name)
        ed_name.setSelection(name.length)
        ed_phone.setText(phone)
        ed_phone.setSelection(phone.length)
        ed_address.setText(address)
        ed_address.setSelection(address.length)
        tv_city.text = mAddress?.area ?: ""
    }

    override fun getPresenter(): AddShipAddressPresenter? {
        return AddShipAddressPresenter(this)
    }

    private fun submitAddress() {
        val name = ed_name.text.toString()
        val phone = ed_phone.text.toString()
        val address = ed_address.text.toString()
        mPresenter?.submitShipAddress(
            name,
            mProvince?.code ?: "",
            mCity?.code ?: "",
            mDistrict?.code ?: "",
            mProvince?.name ?: "",
            mCity?.name ?: "",
            mDistrict?.name ?: "",
            address,
            phone,
            mAddress?.id ?: ""
        )
    }

    override fun onSubmitShipAddressSuccess(address: AddressBean) {
        showToast("提交成功")
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("address", address)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun showDeleteDialog() {
        MessageDialog(this)
            .setMessage("是否删除当前地址？")
            .setOnConfirmClickListener {
                it.dismiss()
                deleteAddress()
            }.show()
    }

    private fun deleteAddress() {
        mPresenter?.deleteAddress(mAddress?.id ?: "")
    }

    override fun onDeleteAddressSuccess(ids: String) {
        showToast("删除地址成功")
        finish()
    }

}