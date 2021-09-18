package com.example.dy;

import androidx.multidex.MultiDexApplication;

import cn.dianyinhuoban.hm.DYHelper;

public class MyApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        DYHelper.init(this);
    }
}
