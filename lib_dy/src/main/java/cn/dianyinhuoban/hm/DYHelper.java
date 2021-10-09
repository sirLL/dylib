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

    public static DYLoginHelper LOGIN_HELPER = null;

    public static void init(Application application, DYLoginHelper loginHelper) {
        MMKV.initialize(application.getApplicationContext());
        ToastUtils.init(application);
        RetrofitServiceManager.initialize(application.getApplicationContext());
        LOGIN_HELPER = loginHelper;
    }

    public static void login(Context context, String userName, String password) {
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

    public static void openLoginPage(Context context, boolean showBack) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackBtn", showBack);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

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

}
