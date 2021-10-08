package cn.dianyinhuoban.hm.mvp.machine.view


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import cn.dianyinhuoban.hm.R
import cn.dianyinhuoban.hm.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.hm.mvp.machine.contract.MachineTypeContract
import cn.dianyinhuoban.hm.mvp.machine.presenter.MachineTypePresenter
import cn.dianyinhuoban.hm.widget.dialog.BaseBottomPicker
import com.hjq.toast.ToastUtils
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.AppManager
import kotlinx.android.synthetic.main.item_machine_type.view.*

class MachineTypePicker : BaseBottomPicker<MachineTypeBean?, MachineTypePresenter?>(),
    MachineTypeContract.View {
    private var mCheckedID: String? = null

    companion object {
        fun newInstance(checkedID: String?): MachineTypePicker {
            val args = Bundle()
            args.putString("checkedID", checkedID)
            val fragment = MachineTypePicker()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_machine_type
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getPresenter(): MachineTypePresenter? {
        return MachineTypePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        if (bundle != null && TextUtils.isEmpty(mCheckedID)) {
            mCheckedID = bundle.getString("checkedID", "-1")
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingView()
        request(DEF_START_PAGE)
    }

    override fun getTitle(): String {
        return "选择机具样式"
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MachineTypeBean?
    ) {
        viewHolder?.itemView?.iv_check_box?.isSelected =
            if (!TextUtils.isEmpty(mCheckedID)) {
                mCheckedID == itemData?.id
            } else {
                false
            }
        viewHolder?.itemView?.tv_title?.text = itemData?.name
    }

    override fun onItemClick(d: MachineTypeBean?, position: Int) {
        d?.let {
            mCheckedID = it.id
            getAdapter()?.notifyDataSetChanged()
            super.onItemClick(d, position)
        }
    }

    fun setCheckedID(checkedID: String?) {
        mCheckedID = checkedID
        getAdapter()?.notifyDataSetChanged()
    }

    override fun request(page: Int) {
        mPresenter?.fetchMachineType()
    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {
        loadData(data)
    }

    override fun showMessage(message: String?) {
        ToastUtils.show(message ?: "")
    }

    override fun onTokenInvalid() {
        AppManager.getInstance().onTokenInvalid(context)
    }

    fun show(manager: FragmentManager, tag: String?, checkedID: String?) {
        if (checkedID != null) {
            mCheckedID = checkedID
        }
        getAdapter()?.notifyDataSetChanged()
        super.show(manager, tag)
    }
}