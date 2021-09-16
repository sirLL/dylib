package com.wareroom.lib_base.net;

import android.content.Context;
import android.text.TextUtils;

import com.wareroom.lib_base.utils.cache.MMKVUtil;
import com.wareroom.lib_http.interceptor.BaseSignInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 签名拦截器
 */
public class SignInterceptor extends BaseSignInterceptor {
    private static final String TAG = "SignInterceptor";

    public SignInterceptor(Context context) {
        super(context);
    }

    //    @Override
//    public Response intercept(Chain chain) throws IOException {
//        //获取到request
//        Request request = chain.request();
//        HttpUrl httpUrl = request.url();
//        HttpUrl.Builder builder = httpUrl.newBuilder();
//
//        //签名参数
//        Map<String, String> signParam = getSignParam();
//        if (signParam != null && signParam.size() > 0) {
//            for (Map.Entry<String, String> entry : signParam.entrySet()) {
//                if (!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(entry.getValue())
//                        && httpUrl.queryParameter(entry.getKey()) == null) {
//                    builder.addQueryParameter(entry.getKey(), entry.getValue());
//                }
//            }
//        }
//        return chain.proceed(request.newBuilder().url(builder.build()).addHeader("uid", MMKVUtil.getToken()).build());
//    }
//
    @Override
    protected Map<String, String> getSignParam() {
        return createSignParam();
    }

    @Override
    protected Map<String, String> getHeaderParam() {
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("uid", MMKVUtil.getToken());
        return headerParam;
    }

    public static Map<String, String> createSignParam() {
        Map<String, String> map = new HashMap<>();
        return map;
    }


}
