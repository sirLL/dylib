
package com.wareroom.lib_base.mvp;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public interface IModel extends LifecycleObserver {

    void onDestroy(LifecycleOwner owner);
}
