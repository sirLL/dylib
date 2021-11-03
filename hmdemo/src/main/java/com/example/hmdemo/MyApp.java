package com.example.hmdemo;

import android.app.Application;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.dianyinhuoban.hm.DYHelper;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DYHelper.init(this);
    }


}
