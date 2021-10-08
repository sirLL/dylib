package cn.dianyinhuoban.hm.mvp.order.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.AddressBean
import cn.dianyinhuoban.hm.mvp.order.contract.AddressManagerContract
import cn.dianyinhuoban.hm.mvp.order.presenter.AddressManagerPresenter
import cn.dianyinhuoban.hm.mvp.setting.view.AddShipAddressActivity
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.dy_fragment_address_manager.*
import kotlinx.android.synthetic.main.dy_item_address_manager.view.*

class AddressManagerFragment : BaseListFragment<AddressBean?, AddressManagerPresenter?>(),
    AddressManagerContract.View {
    private var isPicker = false

    companion object {
        const val RC_ADDRESS_EDIT = 1024
        const val RC_ADDRESS_ADD = 1025
        fun newInstance(isPicker: Boolean): AddressManagerFragment {
            val args = Bundle()
            args.putBoolean("isPicker", isPicker)
            val fragment = AddressManagerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isPicker = arguments?.getBoolean("isPicker") ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            AddShipAddressActivity.openAddShipAddressActivity(this, null, RC_ADDRESS_ADD)
        }
    }

    override fun onStart() {
        super.onStart()
        mRefreshLayout.autoRefresh()
    }

    override fun getPresenter(): AddressManagerPresenter? {
        return AddressManagerPresenter(this)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_address_manager
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_address_manager
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchAddressList(page)
    }

    override fun bindAddressList(data: List<AddressBean>?) {
        loadData(data)
    }


    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: AddressBean?
    ) {
        viewHolder?.itemView?.tv_name?.text = itemData?.name ?: "--"
        viewHolder?.itemView?.tv_phone?.text = itemData?.phone ?: "--"
        viewHolder?.itemView?.tv_address?.text = itemData?.address ?: "--"
        viewHolder?.itemView?.tv_address?.setOnClickListener {
            itemData?.let {
                AddShipAddressActivity.openAddShipAddressActivity(this, it, RC_ADDRESS_EDIT)
            }
        }
    }

    override fun onItemClick(data: AddressBean?, position: Int) {
        data?.let {
            if (isPicker) {
                val intent = Intent()
                val bundle = Bundle()
                bundle.putParcelable("address", it)
                intent.putExtras(bundle)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            } else {
                AddShipAddressActivity.openAddShipAddressActivity(this, it, RC_ADDRESS_EDIT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_ADDRESS_EDIT -> {
                    mRefreshLayout?.autoRefresh()
                }
                RC_ADDRESS_ADD -> {
                    mRefreshLayout?.autoRefresh()
                }
            }
        }
    }
}