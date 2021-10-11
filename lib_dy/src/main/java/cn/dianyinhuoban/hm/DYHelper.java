package cn.dianyinhuoban.hm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;
import com.wareroom.lib_base.net.RetrofitServiceManager;
import com.wareroom.lib_base.utils.cache.MMKVUtil;
import com.wareroom.lib_http.exception.ApiException;
import com.wareroom.lib_http.response.ResponseTransformer;
import com.wareroom.lib_http.schedulers.SchedulerProvider;
import com.wareroom.lib_http.CustomResourceSubscriber;

import cn.dianyinhuoban.hm.api.ApiService;
import cn.dianyinhuoban.hm.mvp.bean.UserBean;
import cn.dianyinhuoban.hm.mvp.home.view.HomeActivity;
import cn.dianyinhuoban.hm.mvp.login.view.LoginActivity;

public class DYHelper {


    public static void init(Application application) {
        MMKV.initialize(application.getApplicationContext());
        ToastUtils.init(application);
        RetrofitServiceManager.initialize(application.getApplicationContext());
    }

    //登录电银泓盟
    public static void loginDYHM(Context context, String userName, String password) {
        ApiService apiService = RetrofitServiceManager.getInstance().getRetrofit().create(ApiService.class);
        apiService.submitLogin(userName, password).compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(new CustomResourceSubscriber<UserBean>() {
                    @Override
                    public void onError(ApiException exception) {
                        ToastUtils.show(exception.getDisplayMessage());
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        super.onNext(userBean);
                        if (userBean != null) {
                            MMKVUtil.saveUserID(userBean.getUid());
                            MMKVUtil.saveUserName(userBean.getUsername());
                            MMKVUtil.saveToken(userBean.getToken());
                            MMKVUtil.savePhone(userBean.getPhone());
                            MMKVUtil.saveNick(userBean.getName());
                            MMKVUtil.saveLoginPassword(password);
                            MMKVUtil.saveInviteCode(userBean.getInviteCode());
                            MMKVUtil.saveAvatar(userBean.getAvatar());
                            context.startActivity(new Intent(context, HomeActivity.class));
                        } else {
                            ToastUtils.show("获取登录信息失败");
                        }
                    }
                });
    }

    //登录电银泓盟
    public static void loginDYHM(Context context, String userName, String password, OnLoginCallBack callBack) {
        ApiService apiService = RetrofitServiceManager.getInstance().getRetrofit().create(ApiService.class);
        apiService.submitLogin(userName, password).compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(new CustomResourceSubscriber<UserBean>() {
                    @Override
                    public void onError(ApiException exception) {
                        if (callBack != null) {
                            callBack.onLoginError(exception == null ? -1 : exception.getCode(), exception == null ? "登录发生了意外" : exception.getDisplayMessage());
                        }
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        super.onNext(userBean);
                        if (userBean != null) {
                            MMKVUtil.saveUserID(userBean.getUid());
                            MMKVUtil.saveUserName(userBean.getUsername());
                            MMKVUtil.saveToken(userBean.getToken());
                            MMKVUtil.savePhone(userBean.getPhone());
                            MMKVUtil.saveNick(userBean.getName());
                            MMKVUtil.saveLoginPassword(password);
                            MMKVUtil.saveInviteCode(userBean.getInviteCode());
                            MMKVUtil.saveAvatar(userBean.getAvatar());
                            context.startActivity(new Intent(context, HomeActivity.class));
                            if (callBack != null) {
                                callBack.onLoginSuccess();
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onLoginError(-1, "获取登录信息失败");
                            }
                        }
                    }
                });
    }

    //打卡电银泓盟
    public static void openDYHM(Context context) {
        String uid = MMKVUtil.getUserID();
        String userName = MMKVUtil.getUserName();
        String token = MMKVUtil.getToken();
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(token)) {
            context.startActivity(new Intent(context, HomeActivity.class));
        } else {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    /**
     * 跳转电银泓盟登录页面
     *
     * @param context
     * @param showBack 是否显示返回按钮
     */
    public static void openLoginPage(Context context, boolean showBack) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackBtn", showBack);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //判断是否登录过电银泓盟，若返回true,调用openDYHM()方法，打开电银泓盟
    public static boolean hasLoggedDYHM() {
        String uid = MMKVUtil.getUserID();
        String userName = MMKVUtil.getUserName();
        String token = MMKVUtil.getToken();
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(token)) {
            return true;
        } else {
            return false;
        }
    }

    //登录电银泓盟回调
    public interface OnLoginCallBack {
        void onLoginSuccess();

        void onLoginError(int code, String message);
    }

}
