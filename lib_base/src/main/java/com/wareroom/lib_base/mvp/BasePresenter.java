
package com.wareroom.lib_base.mvp;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.wareroom.lib_base.utils.AppManager;
import com.wareroom.lib_base.utils.cache.MMKVUtil;
import com.wareroom.lib_http.exception.ApiException;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    protected final String TAG = this.getClass().getSimpleName();
    protected M mModel;
    protected boolean isDestroy;
    private WeakReference<V> mWeakReference;
    protected CompositeDisposable mCompositeDisposable;


    public BasePresenter(V view) {
        this.mModel = buildModel();
        mWeakReference = new WeakReference<>(view);
        onStart();
    }

    protected abstract M buildModel();

    @Override
    public void onStart() {
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        V view = getView();
        if (view != null && view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) view).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
    }

    protected V getView() {
        if (mWeakReference == null) return null;
        return mWeakReference.get();
    }

    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入容器集中处理
    }

    protected void handleError(ApiException e) {
        if (e == null || isDestroy) return;
        getView().hideLoading();
        if (300 == e.getCode()) {
            MMKVUtil.saveToken("");
            getView().onTokenInvalid();
        } else {
            getView().showMessage(e.getMessage());
        }
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        isDestroy = true;
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        owner.getLifecycle().removeObserver(this);
    }


}
