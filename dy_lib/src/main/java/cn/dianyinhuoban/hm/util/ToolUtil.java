package cn.dianyinhuoban.hm.util;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.List;

import cn.dianyinhuoban.hm.bean.BaseAreaBean;


/**
 * Created by Administrator on 2018/6/8 0008.
 */

public class ToolUtil {

    //private List<> var options1Items: List<BaseAreaBean> = ArrayList()
    //private val options2Items: ArrayList<MutableList<BaseAreaBean>> = ArrayList()
    //private val options3Items: ArrayList<MutableList<MutableList<BaseAreaBean>>> = ArrayList()

    private static List<? extends BaseAreaBean> options1Items = null;
    private static List<List<BaseAreaBean>> options2Items = null;
    private static List<List<List<BaseAreaBean>>> options3Items = null;

    public static interface CityOptionListener {
        void onSelectCity(BaseAreaBean province, BaseAreaBean city, BaseAreaBean area);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }




}
