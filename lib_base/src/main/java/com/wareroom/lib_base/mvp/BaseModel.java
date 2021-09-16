package com.wareroom.lib_base.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.wareroom.lib_base.net.RetrofitServiceManager;
import com.wareroom.lib_base.utils.LogUtils;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

public abstract class BaseModel implements IModel {
    protected static String TAG = "BaseModel";
    //网络请求Retrofit
    protected Retrofit mRetrofit;


    public BaseModel() {
        TAG = this.getClass().getSimpleName();
        mRetrofit = RetrofitServiceManager.getInstance().getRetrofit();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
