package com.wareroom.lib_base.net;


import android.content.Context;

import com.wareroom.lib_http.interceptor.BaseSignInterceptor;
import com.wareroom.lib_base.api.BaseURLConfig;
import com.wareroom.lib_base.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static final String TAG = "RetrofitServiceManager";
    private static final int DEFAULT_TIME_OUT = 600;//超时时间 20s
    private static final int DEFAULT_READ_TIME_OUT = 600;

    private Retrofit mRetrofit;
    private Context mContext;
    private static RetrofitServiceManager mRetrofitServiceManager;

    private RetrofitServiceManager(Context context) {
        mContext = context;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.sslSocketFactory(buildSSLSocketFactory());
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

        /**
         * 签名
         */
        BaseSignInterceptor signInterceptor = new SignInterceptor(mContext);
        builder.addInterceptor(signInterceptor);

        /**
         * 日志
         */
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            LogUtils.e(TAG, message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .baseUrl(BaseURLConfig.BASE_URL)
                .build();
    }

    public static void initialize(Context context) {
        if (mRetrofitServiceManager == null) {
            synchronized (RetrofitServiceManager.class) {
                mRetrofitServiceManager = new RetrofitServiceManager(context);
            }
        }
    }

    //获取单例
    public static RetrofitServiceManager getInstance() {
        if (mRetrofitServiceManager == null) {
            throw new NullPointerException("请先调用initialize()初始化");
        }
        return mRetrofitServiceManager;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Context getContext() {
        return mContext;
    }
}
