
package com.wareroom.lib_base.mvp;

public interface IView {

    /**
     * 显示加载
     */
    default void showLoading() {

    }

    default void showLoading(boolean cancelEnable) {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    /**
     * 显示信息
     */
    void showMessage(String message);

    void onTokenInvalid();

}
