package cn.dianyinhuoban.hm.qiyu;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.qiyukf.nimlib.sdk.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.OnBotEventListener;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.event.SDKEvents;
import com.wareroom.lib_base.utils.cache.MMKVUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cn.dianyinhuoban.hm.R;

public class QYHelper {
    private static final String TAG = "QYHelper";

    public static void initQY(Context context) {
        Unicorn.config(context, Config.UNI_APP_KEY, options(), new GlideImageLoader(context));
    }

    public static void openQYService(Context context, String title) {
        ConsultSource source = new ConsultSource("", "电银管家客服", "custom information string");
        source.groupId = Config.GROUP_ID;
        Unicorn.openServiceActivity(context, "电银管家客服", source);
    }


    private static YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.onBotEventListener = new OnBotEventListener() {
            @Override
            public boolean onUrlClick(Context context, String s) {
                Log.d(TAG, "onUrlClick: " + s);
                return super.onUrlClick(context, s);
            }
        };
        options.logSwitch = true;
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        UICustomization uiCustomization = new UICustomization();
//        uiCustomization.leftAvatar = "http://newappapifile.dianyinhuoban.vip/service_head%402x.png";
        uiCustomization.rightAvatar = MMKVUtil.getAvatar();
        uiCustomization.titleCenter = true;
        options.uiCustomization = uiCustomization;
        return options;
    }

    private static SDKEvents configSdkEvent() {
        SDKEvents sdkEvents = new SDKEvents();
        sdkEvents.eventProcessFactory = eventType -> {
            Log.i("YsfDemoApplication", "eventType:" + eventType);
            return null;
        };
        return sdkEvents;
    }

    public static boolean inMainProcess(Context context) {
        String mainProcessName = context.getApplicationInfo().processName;
        String processName = getProcessName();
        return TextUtils.equals(mainProcessName, processName);
    }

    /**
     * 获取当前进程名
     */
    private static String getProcessName() {
        BufferedReader reader = null;
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine().trim();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
