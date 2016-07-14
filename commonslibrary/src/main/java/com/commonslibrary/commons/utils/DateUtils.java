package com.commonslibrary.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * date        :  2016-01-27  13:26
 * author      :  Mickaecle gizthon
 * description :
 */
public class DateUtils {
    /**
     * 时间戳转换成给定格式的时间
     *
     * @param time 时间戳
     *             时间格式
     * @return
     */
    public static String timeStampToFormatString(long time, String formatType) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatType, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 指定格式的时间转换为时间戳
     *
     * @param formatTime 格式化后的时间
     * @param formatType 时间格式
     * @return
     */
    public static long formatStringToTimeStamp(String formatTime, String formatType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatType, Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(formatTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * date format to string ,eg: yyyy-MM-dd  2015-01-10
     *
     * @param date date
     * @return string
     */
    public static String dataToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


}
