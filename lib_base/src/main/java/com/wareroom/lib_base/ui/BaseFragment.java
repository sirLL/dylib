package com.wareroom.lib_base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hjq.toast.ToastUtils;
import com.wareroom.lib_base.mvp.IPresenter;
import com.wareroom.lib_base.mvp.IView;
import com.wareroom.lib_base.utils.AppManager;
import com.wareroom.lib_base.widget.dialog.ProgressDialog;

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView {
    protected P mPresenter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getContentView(), container, false);
        initView(contentView);

        return contentView;
    }

    protected void initView(View contentView) {

    }

    @Override
    public void showLoading() {
        showProgress();
    }

    @Override
    public void hideLoading() {
        dismissProgress();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    protected void showProgress() {
        showProgress(true);
    }

    protected void showProgress(boolean cancelEnable) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(requireContext());
        }
        mProgressDialog.setCanceledOnTouchOutside(cancelEnable);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showToast(String content) {
        ToastUtils.show(content);
    }

    protected abstract P getPresenter();

    protected abstract @LayoutRes
    int getContentView();

    @Override
    public void onTokenInvalid() {
        AppManager.getInstance().onTokenInvalid(getActivity());
    }
}
