

import com.wareroom.lib_http.exception.ApiException;
import com.wareroom.lib_http.exception.CustomException;

import io.reactivex.observers.ResourceObserver;

public abstract class CustomResourceSubscriber<T> extends ResourceObserver<T> {
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {
        ApiException exception = CustomException.handleException(t);
        onError(exception);
    }

    @Override
    public void onComplete() {

    }

    public abstract void onError(ApiException exception);
}
