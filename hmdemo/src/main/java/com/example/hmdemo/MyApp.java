package com.example.hmdemo;

import android.app.Application;

import cn.dianyinhuoban.hm.DYHelper;
import cn.dianyinhuoban.hm.DYLoginHelper;

public class MyApp extends Application {

    private DYLoginHelper loginHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        loginHelper = new DYLoginHelper() {
            @Override
            public void checkUserName(String userName, String password) {
                checkUser(userName, password);
            }
        };
        DYHelper.init(this, loginHelper);
    }


    private void checkUser(String userName, String password) {
        //调接口
        //以下代码写到接口回调里面（UI线程）
        boolean isDYHMUser = true;//是否电银泓盟版用户
        if (isDYHMUser) {
            loginHelper.loginDYHM(getApplicationContext(), userName, password);
        } else {
            login(userName, password);
        }
    }

    private void login(String userName, String password) {
        //调用你们的登录接口
        //以下代码写到接口回调里面（UI线程）
        loginHelper.closeDYHMLoginPage();
    }

}
