package com.wareroom.lib_base.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {

    private static int MILL_MIN = 1000 * 60;
    private static int MILL_HOUR = MILL_MIN * 60;
    private static int MILL_DAY = MILL_HOUR * 24;

    private static Calendar msgCalendar = null;

    public final static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public final static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String PATTERN_YYYY_MM_DD_CHAR = "yyyy年MM月dd日";

    public final static String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String PATTERN_YYYY_MM = "yyyy-MM";
    public final static String PATTERN_MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String PATTERN_YYYY = "yyyy";
    public static final String PATTERN_MM_DD = "MM-dd";
    public static final String PATTERN_HH_MM = "HH:mm";

    public static String getYYYYMMDDHHMMSS(long mills) {
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        msgCalendar.setTimeZone(TimeZone.getDefault());
        msgCalendar.setTime(new Date(mills));
        return new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS).format(msgCalendar.getTime());
    }

    public static String getYYYYMMDDHHMM(long mills) {
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        msgCalendar.setTimeZone(TimeZone.getDefault());
        msgCalendar.setTime(new Date(mills));
        return new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM).format(msgCalendar.getTime());
    }

    public static String getCurrentDate(String pattern) {
        Calendar instance = Calendar.getInstance();
        return new SimpleDateFormat(pattern).format(instance.getTime());
    }

    public static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String formatDate(long mills, String pattern) {
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        msgCalendar.setTimeZone(TimeZone.getDefault());
        msgCalendar.setTime(new Date(mills));
        return new SimpleDateFormat(pattern).format(new Date(mills));
    }


    public static String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String stringForSecond(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        return mFormatter.format("%02d'%02d\"", minutes, seconds).toString();
    }

    public static String getCommentTime(long time) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(time);

        long todayTime = todayCalendar.getTimeInMillis() / 1000;
        long otherTime = todayCalendar.getTimeInMillis() / 1000;
        int todayYear = todayCalendar.get(Calendar.YEAR);
        int todayMonth = todayCalendar.get(Calendar.MONTH) + 1;
        int todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH);

        int otherYear = otherCalendar.get(Calendar.YEAR);
        int otherMonth = otherCalendar.get(Calendar.MONTH) + 1;
        int otherDay = otherCalendar.get(Calendar.DAY_OF_MONTH);

        if (todayTime - otherTime < 60) {
            return "刚刚";
        } else if (todayTime - otherTime < 60 * 60) {
            return String.format("%s分钟前", String.valueOf((todayTime - otherTime) / 60));
        } else if (todayTime - otherTime < 60 * 60 * 24) {
            return String.format("%s小时前", String.valueOf((todayTime - otherTime) * 24 / 60));
        } else if (todayYear == otherYear && todayMonth == otherMonth && todayDay - otherDay == 1) {
            return "昨天";
        } else if (todayTime - otherTime < 60 * 60 * 24 * 5) {
            return "昨天";
        } else {
            return formatDate(otherTime * 1000, PATTERN_YYYY_MM_DD);
        }
    }


    /**
     * 判断用户的设备时区是否为东八区（中国）
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultValue = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08")) {
            defaultValue = true;
        } else {
            defaultValue = false;
        }
        return defaultValue;
    }

    /**
     * 根据不同时区，转换时间
     */
    public static Date transformTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime()) - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;
    }


    /**
     * Long转Date
     *
     * @param currentTime
     * @return
     */
    public static Date long2Date(long currentTime) {
        Date date = new Date(currentTime);
        return date;
    }

    /**
     * 显示日期为x小时前、昨天、前天、x天前等
     *
     * @param longTime
     * @return
     */
    public static String showTimeAhead(long longTime) {
        String resultTime = "";

        //传入的日期
        Date date = null;
        if (isInEasternEightZones()) {
            date = long2Date(longTime);
        } else {
            date = transformTimeZone(long2Date(longTime), TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }

        //当前日期
        Date nowDate = new Date();
        int days = (int) (nowDate.getDay() - date.getDay());

        //如果日期相同
        if (days == 0) {
            int minutes = nowDate.getMinutes() - date.getMinutes();
            if (minutes == 0) {
                resultTime = "刚刚";
            } else {
                int hour = nowDate.getHours() - date.getHours();
                //如果小时相同
                if (hour == 0) {
                    resultTime = (nowDate.getMinutes() - date.getMinutes()) + "分钟前";
                } else {
                    resultTime = hour + "小时前";
                }
            }
        } else if (days == 1) {
            resultTime = "昨天";
        } else if (days == 2) {
            resultTime = "前天 ";
        } else if (days > 2 && days < 31) {
            resultTime = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            resultTime = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            resultTime = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            resultTime = "3个月前";
        } else {
            resultTime = long2String(longTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
        }
        return resultTime;
    }

    /**
     * Long转String
     *
     * @param currentTime
     * @param formatType
     * @return
     */
    public static String long2String(long currentTime, String formatType) {
        return new SimpleDateFormat(formatType).format(currentTime);
    }

    public static String formatCountDownTime(long time) {
        final int MINUTE = 60;
        final int HOUR = 60 * 60;

        String hour_ = "";
        String minute_ = "";
        String second_ = "";

        if (time >= 0) {
            long hour = time / HOUR;
            long minute = time % HOUR / MINUTE;
            long second = time % MINUTE;

            if (hour >= 10) {
                hour_ = String.valueOf(hour);
            } else {
                hour_ = "0" + String.valueOf(hour);
            }
            if (minute >= 10) {
                minute_ = String.valueOf(minute);
            } else {
                minute_ = "0" + String.valueOf(minute);
            }
            if (second >= 10) {
                second_ = String.valueOf(second);
            } else {
                second_ = "0" + String.valueOf(second);
            }
        } else {
            hour_ = "--";
            minute_ = "--";
            second_ = "--";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(hour_);
        builder.append(":");
        builder.append(minute_);
        builder.append(":");
        builder.append(second_);
        return builder.toString();
    }


    public static List<String> formatPkEndTime(String endTime) {
        List<String> strings = new ArrayList<String>();
        strings.add(0,"00");
        strings.add(1,"00");
        strings.add(2,"00");

        if (TextUtils.isEmpty(endTime)) {
            return strings;
        }

        long time =  Long.parseLong(endTime) - (System.currentTimeMillis() / 1000);

        if (time <= 0) {
            return strings;
        }

        long days = time / 86400;

        long hours =  ( time - (days * 86400)) / 3600;

        long minutes = (time - days * 86400 - hours * 3600) / 60;

        strings.add(0,String.valueOf(days));
        strings.add(1,String.valueOf(hours));
        strings.add(2,String.valueOf(minutes));

        return strings;
    }

}
