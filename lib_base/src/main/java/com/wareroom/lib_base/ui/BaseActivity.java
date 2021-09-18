package com.wareroom.lib_base.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.wareroom.lib_base.R;
import com.wareroom.lib_base.mvp.IPresenter;
import com.wareroom.lib_base.mvp.IView;
import com.wareroom.lib_base.utils.AppManager;
import com.wareroom.lib_base.widget.dialog.ProgressDialog;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {
    protected P mPresenter;
    private ProgressDialog mProgressDialog;

    protected RelativeLayout mToolbar;
    private FrameLayout mPageContainer;
    private ImageView mBackButton;
    private TextView mPageTitleTextView;
    private TextView mRightTextButton;
    private ImageView mRightImageButton;
    private ImageView mRightImageButton_;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onCreateBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        initStatusBar();
        initRootView();
        handleIntent(getIntent().getExtras());
        mPresenter = getPresenter();
    }

    protected void onCreateBefore(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 处理页面跳转的传值
     *
     * @param bundle
     */
    protected void handleIntent(Bundle bundle) {

    }

    //是否是深色模式
    protected boolean isDarkModeEnable() {
        return true;
    }

    //Toolbar是否可用
    protected boolean toolbarIsEnable() {
        return true;
    }

    /**
     * 初始化状态栏
     */
    protected void initStatusBar() {
        ImmersionBar.with(this)
                .autoDarkModeEnable(isDarkModeEnable())
                .autoStatusBarDarkModeEnable(isDarkModeEnable())
                .statusBarColor(getStatusBarColor())
                .flymeOSStatusBarFontColor(getStatusBarColor())
                .init();
    }

    private void initRootView() {
        super.setContentView(getRootView());
        mToolbar = findViewById(R.id.toolbar);
        mPageContainer = findViewById(R.id.base_content_container);
        initToolbar();
    }

    protected @LayoutRes
    int getRootView() {
        return R.layout.dy_activity_base;
    }


    @Override
    public void setContentView(int layoutResID) {
        View pageView = LayoutInflater.from(this).inflate(layoutResID, mPageContainer, false);
        mPageContainer.removeAllViews();
        mPageContainer.addView(pageView);
    }

    @Override
    public void setContentView(View view) {
        mPageContainer.removeAllViews();
        mPageContainer.addView(view);
    }

    //初始化 Toolbar
    protected void initToolbar() {
        View toolbar = findViewById(R.id.toolbar);
        toolbar.setVisibility(toolbarIsEnable() ? View.VISIBLE : View.GONE);
        toolbar.setBackgroundColor(getToolbarColor());
        mBackButton = findViewById(R.id.base_iv_back);
        mPageTitleTextView = findViewById(R.id.base_tv_title);
        mRightTextButton = findViewById(R.id.base_tv_right);
        mRightImageButton = findViewById(R.id.base_iv_right);
        mRightImageButton_ = findViewById(R.id.base_iv_right_);

        //返回键图标
        if (getBackButtonIcon() > 0) {
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setImageResource(getBackButtonIcon());
            mBackButton.setOnClickListener(v -> {
                onBackClick();
            });
        } else {
            //若资源ID<=0 隐藏返回按钮
            mBackButton.setVisibility(View.GONE);
        }
    }

    protected void onBackClick() {
        onBackPressed();
    }

    /**
     * toolbar 颜色
     *
     * @return
     */
    protected @ColorInt
    int getToolbarColor() {
        return Color.WHITE;
    }

    protected @ColorRes
    int getStatusBarColor() {
        return R.color.dy_base_color_white;
    }

    //获取返回键图标
    protected int getBackButtonIcon() {
        return R.drawable.dy_base_ic_back_black;
    }

    /**
     * 设置页面标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        setTitle(title, ContextCompat.getColor(this, R.color.dy_color_base_page_title));
    }

    /**
     * 设置页面标题
     *
     * @param title
     * @param color
     */
    protected void setTitle(String title, @ColorInt int color) {
        mPageTitleTextView.setText(TextUtils.isEmpty(title) ? "" : title);
        mPageTitleTextView.setTextColor(color);
    }

    /**
     * Toolbar 右边文字按钮
     *
     * @param text
     * @param onClickListener
     */
    protected void setRightButtonText(String text, View.OnClickListener onClickListener) {
        setRightButtonText(text, ContextCompat.getColor(this, R.color.dy_color_base_page_title), onClickListener);
    }

    /**
     * Toolbar 右边文字按钮
     *
     * @param text
     * @param color
     * @param onClickListener
     */
    protected void setRightButtonText(String text, @ColorInt int color,
                                      View.OnClickListener onClickListener) {
        mRightTextButton.setText(text);
        mRightTextButton.setTextColor(color);
        mRightTextButton.setOnClickListener(onClickListener);
    }

    /**
     * Toolbar 右边第一个图片按钮
     *
     * @param icon
     * @param onClickListener
     */
    protected void setRightIcon(@DrawableRes int icon, View.OnClickListener onClickListener) {
        mRightImageButton.setVisibility(View.VISIBLE);
        mRightImageButton.setImageResource(icon);
        mRightImageButton.setOnClickListener(onClickListener);
    }

    /**
     * Toolbar 右边第二个图片按钮
     *
     * @param icon
     * @param onClickListener
     */
    protected void setRightIcon_(@DrawableRes int icon, View.OnClickListener onClickListener) {
        mRightImageButton_.setImageResource(icon);
        mRightImageButton_.setOnClickListener(onClickListener);
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
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
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

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        try {
            View v = getCurrentFocus();
            if (v != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(v.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                //强制隐藏键盘
                /*((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(v.getWindowToken(), 0);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract P getPresenter();

    @Override
    public void onTokenInvalid() {
        AppManager.getInstance().onTokenInvalid(this);
    }

    @Override
    protected void onDestroy() {
        dismissProgress();
        super.onDestroy();
    }
}
