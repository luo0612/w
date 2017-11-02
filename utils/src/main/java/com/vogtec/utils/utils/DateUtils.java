package com.vogtec.utils.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/8/31.
 * 日期工具类
 */
public abstract class DateUtils {
    /**
     * 格式格式化日期
     *
     * @param time   需要被格式化的日期
     * @param format 格式 如:"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String formatTime(long time, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String formatTime = dateFormat.format(new Date(time));
        return formatTime;
    }

    /**
     * 获取时间戳格式的时间
     *
     * @param date   需要被转换的时间
     * @param format 被转换的时间的格式 如:"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long getTime(String date, String format) {
        long time = -1;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date parseDate = dateFormat.parse(date);
            time = parseDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
