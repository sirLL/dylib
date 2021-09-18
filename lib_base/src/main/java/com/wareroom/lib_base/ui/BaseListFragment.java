package com.wareroom.lib_base.ui;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wareroom.lib_base.R;
import com.wareroom.lib_base.mvp.IPresenter;
import com.wareroom.lib_base.ui.adapter.SimpleAdapter;
import com.wareroom.lib_base.utils.DimensionUtils;
import com.wareroom.lib_base.utils.NetWorksUtils;
import com.wareroom.lib_base.widget.DividerDecoration;
import com.wareroom.lib_base.widget.LoadingLayout;

import java.util.List;


public abstract class BaseListFragment<T, P extends IPresenter> extends BaseFragment<P>
        implements OnRefreshListener, OnLoadMoreListener, LoadingLayout.OnViewClickListener {
    protected LoadingLayout mLoadingLayout;
    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected SimpleAdapter<T> mAdapter;

    protected static final int DEF_START_PAGE = 1;
    protected int mCurrentPage = DEF_START_PAGE;
    private boolean isLoadMore = false;

    protected void initView(View contentView) {
        mLoadingLayout = contentView.findViewById(R.id.load_layout);
        mRefreshLayout = contentView.findViewById(R.id.refresh_layout);
        mRecyclerView = contentView.findViewById(R.id.recycler_view);
        initAdapter();
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.addItemDecoration(getItemDecoration());
        mRecyclerView.setAdapter(mAdapter);

        //下拉刷新
        boolean isSupportRefresh = isSupportRefresh();
        mRefreshLayout.setEnableRefresh(isSupportRefresh);
        if (isSupportRefresh) {
            mRefreshLayout.setOnRefreshListener(this);
            mRefreshLayout.setRefreshHeader(getRefreshHeader());
        }
        //加载更多
        boolean isSupportLoadMore = isSupportLoadMore();
        mRefreshLayout.setEnableLoadMore(isSupportLoadMore);
        if (isSupportLoadMore) {
            mRefreshLayout.setOnLoadMoreListener(this);
            mRefreshLayout.setRefreshFooter(getLoadMoreFooter());
        }
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    @Override
    protected int getContentView() {
        return R.layout.dy_base_fragment_list;
    }

    protected boolean isSupportRefresh() {
        return true;
    }

    protected boolean isSupportLoadMore() {
        return true;
    }

    protected RefreshFooter getLoadMoreFooter() {
        return new ClassicsFooter(getContext());
    }

    protected RefreshHeader getRefreshHeader() {
        return new ClassicsHeader(getContext());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerDecoration(ContextCompat.getColor(getContext(), R.color.dy_color_divider),
                DimensionUtils.dp2px(getContext(), 1));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = false;
        mCurrentPage = DEF_START_PAGE;
        if (!NetWorksUtils.isConnected(getActivity())) {
            mLoadingLayout.showNetworkUnEnable();
            refreshLayout.finishRefresh();
            if (isSupportLoadMore()) {
                refreshLayout.setEnableLoadMore(false);
            }
        } else {
            onRequest(mCurrentPage);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = true;
        mCurrentPage += 1;
        onRequest(mCurrentPage);
    }

    protected void loadData(List<T> data) {
        if (isLoadMore) {
            mAdapter.appendData(data);
            mRefreshLayout.finishLoadMore();
        } else {
            mAdapter.setData(data);
            mRefreshLayout.finishRefresh();
            if (data == null || data.size() == 0) {
                mLoadingLayout.showEmpty();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mLoadingLayout.showSuccess();
                if (isSupportLoadMore()) {
                    mRefreshLayout.setEnableLoadMore(true);
                }
            }
        }

    }


    private void initAdapter() {
        mAdapter = new SimpleAdapter<T>(getItemLayout()) {
            @Override
            public void convert(SimpleViewHolder viewHolder, int position, T itemData) {
                BaseListFragment.this.convert(viewHolder, position, itemData);
            }

            @Override
            public void onItemClick(T data, int position) {
                BaseListFragment.this.onItemClick(data, position);
            }
        };
    }

    protected void showLoadError() {
        mLoadingLayout.showError();
        if (isSupportLoadMore()) {
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void showLoadError(String error) {
        mLoadingLayout.showError(error);
        if (isSupportLoadMore()) {
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void showEmpty() {
        mLoadingLayout.showEmpty();
        if (isSupportLoadMore()) {
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void showEmpty(String message) {
        mLoadingLayout.showEmpty(message);
        if (isSupportLoadMore()) {
            mRefreshLayout.setEnableLoadMore(false);
        }
    }

    protected void showLoadingView() {
        mLoadingLayout.showLoading();
    }

    protected void showLoadingView(String message) {
        mLoadingLayout.showLoading(message);
    }

    @Override
    public void onReloadClick() {
        mCurrentPage = 1;
        onRequest(mCurrentPage);
    }

    protected abstract void onRequest(int page);

    protected abstract @LayoutRes
    int getItemLayout();

    protected abstract void convert(SimpleAdapter.SimpleViewHolder viewHolder, int position, T itemData);

    protected abstract void onItemClick(T data, int position);
}
