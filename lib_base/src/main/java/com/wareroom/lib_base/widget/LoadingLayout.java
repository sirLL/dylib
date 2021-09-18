package com.wareroom.lib_base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wareroom.lib_base.R;

public class LoadingLayout extends FrameLayout {
    private View mContentLayout;
    private View mLoadingLayout;
    private View mErrorLayout;
    private View mEmptyLayout;
    private View mNetworkLayout;

    private OnViewClickListener mOnViewClickListener;

    public LoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("LoadingLayout can host only one direct child");
        }
        mContentLayout = this.getChildAt(0);
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mLoadingLayout = layoutInflater.inflate(R.layout.dy_base_view_loading, this, false);
        mErrorLayout = layoutInflater.inflate(R.layout.dy_base_view_error, this, false);
        mNetworkLayout = layoutInflater.inflate(R.layout.dy_base_view_network_unenable, this, false);
        mEmptyLayout = layoutInflater.inflate(R.layout.dy_base_view_empty, this, false);
    }

    public void showLoading() {
        showLoading("数据加载中，请稍后");
    }

    public void showLoading(String message) {
        TextView tvMessage = mLoadingLayout.findViewById(R.id.tv_message);
        ProgressView progressView = mLoadingLayout.findViewById(R.id.base_loading_progress);
        tvMessage.setText(message);
        progressView.resetCount();
        progressView.setText("电银");
        progressView.startSpinning();
        removeAllViews();
        addView(mLoadingLayout);
    }

    public void showError() {
        showError("数据加载错误");
    }

    public void showError(String message) {
        TextView tvMessage = mErrorLayout.findViewById(R.id.tv_message);
        mErrorLayout.findViewById(R.id.tv_reload).setOnClickListener(v -> {
            if (mOnViewClickListener != null) {
                mOnViewClickListener.onReloadClick();
            }
            showLoading();
        });
        tvMessage.setText(message);
        removeAllViews();
        addView(mErrorLayout);
    }

    public void showNetworkUnEnable() {
        showNetworkUnEnable("暂无网络");
    }

    public void showNetworkUnEnable(String message) {
        TextView tvMessage = mNetworkLayout.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        removeAllViews();
        addView(mNetworkLayout);
    }

    public void showEmpty() {
        showEmpty("暂无数据");
    }

    public void showEmpty(String message) {
        TextView tvMessage = mEmptyLayout.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        removeAllViews();
        addView(mEmptyLayout);
    }

    public void showSuccess() {
        removeAllViews();
        addView(mContentLayout);
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        mOnViewClickListener = onViewClickListener;
    }

    public interface OnViewClickListener {
        void onReloadClick();
    }
}
