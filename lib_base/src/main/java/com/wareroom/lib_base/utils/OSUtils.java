package com.wareroom.lib_base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;

public class OSUtils {

    public static String getDeviceId(Context context) {
        String deviceId = MMKV.mmkvWithID("DEVICE_INFO").decodeString("DEVICE_ID", "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(deviceId)) {
                Random random = new Random();
                deviceId = Calendar.getInstance().getTimeInMillis() + "_" + random.nextInt(999999 - 100000 + 1) + 100000;
            }
            MMKV.mmkvWithID("DEVICE_INFO").encode("DEVICE_ID", deviceId);
        }

        return deviceId;
    }

    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }


}