
package com.wareroom.lib_base.mvp;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public interface IPresenter extends LifecycleObserver {

    /**
     * 做一些初始化操作
     */
    void onStart();


    void onDestroy(LifecycleOwner owner);
}
