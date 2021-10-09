package cn.dianyinhuoban.hm;

import android.content.Context;
import android.content.Intent;

import com.hjq.toast.ToastUtils;
import com.wareroom.lib_base.net.RetrofitServiceManager;
import com.wareroom.lib_base.utils.cache.MMKVUtil;
import com.wareroom.lib_http.CustomResourceSubscriber;
import com.wareroom.lib_http.exception.ApiException;
import com.wareroom.lib_http.response.ResponseTransformer;
import com.wareroom.lib_http.schedulers.SchedulerProvider;

import org.greenrobot.eventbus.EventBus;

import cn.dianyinhuoban.hm.api.ApiService;
import cn.dianyinhuoban.hm.event.CloseLoadingEvent;
import cn.dianyinhuoban.hm.event.CloseLoginPageEvent;
import cn.dianyinhuoban.hm.event.ShowLoadingEvent;
import cn.dianyinhuoban.hm.mvp.bean.UserBean;
import cn.dianyinhuoban.hm.mvp.home.view.HomeActivity;

public abstract class DYLoginHelper {

    //同步验证
    public abstract void checkUserName(String userName, String password);

    //登录电银泓盟版
    public void loginDYHM(Context context, String userName, String password) {
        ApiService apiService = RetrofitServiceManager.getInstance().getRetrofit().create(ApiService.class);
        apiService.submitLogin(userName, password).compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(new CustomResourceSubscriber<UserBean>() {
                    @Override
                    public void onError(ApiException exception) {
                        closeLoading();
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
                            closeDYHMLoginPage();
                        } else {
                            closeLoading();
                            ToastUtils.show("获取登录信息失败");
                        }
                    }
                });
    }

    //关闭电银泓盟版登录页面
    public void closeDYHMLoginPage() {
        closeLoading();
        EventBus.getDefault().post(new CloseLoginPageEvent());
    }

    private void showLoading() {
        EventBus.getDefault().post(new ShowLoadingEvent());
    }

    private void closeLoading() {
        EventBus.getDefault().post(new CloseLoadingEvent());
    }
}
