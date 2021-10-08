package cn.dianyinhuoban.hm;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;
import com.wareroom.lib_base.net.RetrofitServiceManager;
import com.wareroom.lib_base.utils.cache.MMKVUtil;

public class DYHelper {
    public static void init(Application application) {
        MMKV.initialize(application.getApplicationContext());
        ToastUtils.init(application);
        MMKVUtil.saveUserID("7");
        MMKVUtil.saveUserName("15202353939");
        MMKVUtil.saveToken("s5wfRzRXXX7AtEQuwzghCDEDerzSdKNGzHVbUnPMvEJvru7j4mbStsWehP5ENsTm");
        MMKVUtil.savePhone("15202353939");
        MMKVUtil.saveNick("吗咿呀嘿");
//        MMKVUtil.saveLoginPassword(password);
        MMKVUtil.saveInviteCode("123");
        MMKVUtil.saveAvatar("");
        RetrofitServiceManager.initialize(application.getApplicationContext());
    }
}
