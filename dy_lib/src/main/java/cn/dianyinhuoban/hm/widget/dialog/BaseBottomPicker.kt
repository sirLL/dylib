package cn.dianyinhuoban.hm.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import cn.dianyinhuoban.hm.R
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.AppManager
import com.wareroom.lib_base.widget.LoadingLayout
import kotlinx.android.synthetic.main.activity_member.*
import kotlinx.android.synthetic.main.base_bottom_picker.*


abstract class BaseBottomPicker<D, P : IPresenter?> : DialogFragment(), OnRefreshListener,
    OnLoadMoreListener,
    LoadingLayout.OnViewClickListener, IView {

    private var mCurrentPage = DEF_START_PAGE
    protected var mAdapter: SimpleAdapter<D>? = null
    private var mOnItemSelectedListener: OnItemSelectedListener<D, P>? = null
    private var isLoadMore = false
    protected var mPresenter: P? = null

    companion object {
        const val DEF_START_PAGE = 1;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mPresenter = getPresenter()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(getContentLayoutRes(), container, false)
    }

    protected open fun getContentLayoutRes(): Int {
        return R.layout.base_bottom_picker
    }

    protected open fun isSupportLoadMore(): Boolean {
        return true
    }

    protected open fun getTitle(): String {
        return ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_cancel.setOnClickListener {
            dismiss()
        }
        tv_title.text = getTitle()
        setupRefreshLayout()
        setupLoadLayout()
        setupRecyclerView()
    }

    fun setOnItemSelectedListener(itemSelectedListener: OnItemSelectedListener<D, P>) {
        mOnItemSelectedListener = itemSelectedListener
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val layoutParams = window?.attributes
        setWindowLayoutParams(layoutParams, window)
    }

    protected open fun showLoadingView() {
        loading_layout.showLoading()
    }

    protected open fun showEmpty() {
        loading_layout.showEmpty()
    }

    protected open fun showError(error: String) {
        loading_layout.showError(error)
    }

    protected open fun setWindowLayoutParams(
        layoutParams: WindowManager.LayoutParams?,
        window: Window?,
    ) {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setWindowAnimations(R.style.BaseBottomDialogFragment)
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = layoutParams
    }

    private fun setupRefreshLayout() {
        val refreshHeader = ClassicsHeader(context)
        val refreshFooter = ClassicsFooter(context)
        refresh_layout.setRefreshHeader(refreshHeader)
        refresh_layout.setRefreshFooter(refreshFooter)
        refresh_layout.setOnRefreshListener(BaseBottomPicker@ this)
        refresh_layout.setOnLoadMoreListener(BaseBottomPicker@ this)
        refresh_layout.setHeaderTriggerRate(0.7f)
        refresh_layout.setFooterTriggerRate(0.7f)
        refresh_layout.setEnableLoadMore(isSupportLoadMore())
    }

    private fun setupLoadLayout() {
        loading_layout.setOnViewClickListener(BaseBottomPicker@ this)
    }

    protected open fun setupRecyclerView() {
        mAdapter = object : SimpleAdapter<D>(getItemLayoutRes()) {
            override fun convert(viewHolder: SimpleViewHolder?, position: Int, itemData: D?) {
                this@BaseBottomPicker.convert(viewHolder, position, itemData)
            }

            override fun onItemClick(data: D?, position: Int) {
                this@BaseBottomPicker.onItemClick(data, position)
            }

            override fun getItemCount(): Int {
                return if (mData == null) {
                    0
                } else {
                    mData.size
                }
            }
        }
        recycler_view.adapter = mAdapter
    }

    protected open fun onItemClick(d: D?, position: Int) {
        mOnItemSelectedListener?.onItemSelected(this@BaseBottomPicker, d, position)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        isLoadMore = false
        mCurrentPage = DEF_START_PAGE
        request(mCurrentPage)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isLoadMore = true
        request(mCurrentPage)
    }

    override fun onReloadClick() {
        isLoadMore = false
        mCurrentPage = DEF_START_PAGE
        request(mCurrentPage)
    }


    abstract fun getItemLayoutRes(): Int

    abstract fun getPresenter(): P?

    abstract fun convert(viewHolder: SimpleAdapter.SimpleViewHolder?, position: Int, itemData: D?)

    abstract fun request(page: Int)

    protected fun showToast(message: String) {
        ToastUtils.show(message)
    }

    protected fun loadData(data: List<D>?) {
        if (isLoadMore) {
            mAdapter?.appendData(data)
            refresh_layout?.finishLoadMore()
        } else {
            mAdapter?.data = data
            refresh_layout?.finishRefresh()
            if (data == null || data.isEmpty()) {
                loading_layout?.showEmpty()
                refresh_layout?.setEnableLoadMore(false)
            } else {
                mCurrentPage += 1
                loading_layout?.showSuccess()
                if (isSupportLoadMore()) {
                    refresh_layout?.setEnableLoadMore(true)
                }
            }
        }
    }

    protected open fun getAdapter(): SimpleAdapter<D>? {
        return mAdapter
    }

    interface OnItemSelectedListener<T, P : IPresenter?> {
        fun onItemSelected(bottomPicker: BaseBottomPicker<T, P>, t: T?, position: Int)
    }

    override fun showMessage(message: String?) {
        showToast(message ?: "")
    }

    override fun onTokenInvalid() {
        AppManager.getInstance().onTokenInvalid(context)
    }
}