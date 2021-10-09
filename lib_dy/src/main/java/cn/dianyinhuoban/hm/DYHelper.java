package cn.dianyinhuoban.hm;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;
import com.wareroom.lib_base.net.RetrofitServiceManager;
import com.wareroom.lib_base.utils.cache.MMKVUtil;
import com.wareroom.lib_http.exception.ApiException;
import com.wareroom.lib_http.response.ResponseTransformer;
import com.wareroom.lib_http.schedulers.SchedulerProvider;
import com.wareroom.lib_http.CustomResourceSubscriber;

import java.lang.ref.WeakReference;

import cn.dianyinhuoban.hm.api.ApiService;
import cn.dianyinhuoban.hm.mvp.bean.UserBean;

public class DYHelper {
    public static void init(Application application) {
        MMKV.initialize(application.getApplicationContext());
        ToastUtils.init(application);
        RetrofitServiceManager.initialize(application.getApplicationContext());
    }

    public static void login(String userName, String password, LoginCallback callback) {
        WeakReference<LoginCallback> weakReference = new WeakReference<>(callback);
        ApiService apiService = RetrofitServiceManager.getInstance().getRetrofit().create(ApiService.class);
        apiService.submitLogin(userName, password).compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(new CustomResourceSubscriber<UserBean>() {
                    @Override
                    public void onError(ApiException exception) {
                        if (weakReference.get() != null) {
                            weakReference.get().onLoginError(exception.getCode(), exception.getDisplayMessage());
                        }
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        super.onNext(userBean);
                        if (weakReference.get() != null) {
                            if (userBean != null) {
                                MMKVUtil.saveUserID(userBean.getUid());
                                MMKVUtil.saveUserName(userBean.getUsername());
                                MMKVUtil.saveToken(userBean.getToken());
                                MMKVUtil.savePhone(userBean.getPhone());
                                MMKVUtil.saveNick(userBean.getName());
                                MMKVUtil.saveLoginPassword(password);
                                MMKVUtil.saveInviteCode(userBean.getInviteCode());
                                MMKVUtil.saveAvatar(userBean.getAvatar());
                                weakReference.get().onLoginSuccess(userBean);
                            } else {
                                weakReference.get().onLoginError(0, "获取登录信息失败");
                            }
                        }
                    }
                });
    }


    public interface LoginCallback {
        void onLoginSuccess(UserBean userBean);

        void onLoginError(int code, String msg);
    }

}
