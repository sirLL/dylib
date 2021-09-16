package com.wareroom.lib_base.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

public class NumberUtils {

    public static BigDecimal string2BigDecimal(String value) {
        if (TextUtils.isEmpty(value)) {
            value = "0";
        }
        return new BigDecimal(value);
    }

    public static String numberScale(String money) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static String numberScale(String money, int roundingMode) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.setScale(2, roundingMode).toPlainString();
    }

    public static String numberScale(double money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static String numberScale(double money, int roundingMode) {
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal.setScale(2, roundingMode).toPlainString();
    }

    public static String formatMoney(String money) {
        if (TextUtils.isEmpty(money)) {
            money = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(money);
        if (bigDecimal.doubleValue() >= 10000) {
            return bigDecimal.divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_HALF_UP)
                    .setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "w";
        }
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static String formatMoney(double money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        if (bigDecimal.doubleValue() >= 10000) {
            return bigDecimal.divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_HALF_UP)
                    .setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "w";
        }
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static String formatMoney(BigDecimal money) {
        BigDecimal bigDecimal;
        if(money==null){
            bigDecimal= new BigDecimal(0);
        }else {
            bigDecimal=money;
        }
        if (bigDecimal.doubleValue() >= 10000) {
            return bigDecimal.divide(BigDecimal.valueOf(10000), 4, BigDecimal.ROUND_HALF_UP)
                    .setScale(2, BigDecimal.ROUND_DOWN).toPlainString() + "w";
        }
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }
}
