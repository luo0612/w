package com.vogtec.utils.utils;

/**
 * Created by admin on 2016/8/31.
 * 时间工具类
 */
public abstract class TimeUtils {
    /**
     * 持续时间格式化
     *
     * @param time    需要格式化的时间长度,如:100000毫秒
     * @param connect 连接符号 如:"-",":"
     * @return 格式化后的事件 如:"hh-mm-ss","hh:mm:ss","mm-ss","mm:ss"
     */
    public static String getFormatHHMMSS(long time, String connect) {
        time = time / 1000;
        long hour = time / 3600;
        String hourStr = getHourString(hour);
        long minute = (time - hour * 3600) / 60;
        String minuteStr = getMinuteString(minute);
        long second = time % 60;
        String secondStr = getSecondString(second);
        return hourStr + connect + minuteStr + connect + secondStr;
    }


    /**
     * 持续时间格式化
     * HHMMSS:时分秒
     * MMSSMM:分秒毫秒
     *
     * @param time
     * @param connect
     * @return
     */
    public static String getFormatHHMMSSOrMMSSMM(long time, String connect) {
        //获取毫秒
        long millis = time % 1000 / 10;
        //获取秒
        long s = time / 1000 % 60;
        //获取分钟
        long m = time / (60 * 1000) % 60;
        //获取小时
        long h = time / (60 * 60 * 1000);

        //1.判断持续时间是否大于1个小时=1*60*60*1000;
        if (h > 0) {
            //说明时间大于1个小时,显示HHMMSS=时分秒
            return getFormatValue(h) + connect + getFormatValue(m) + connect + getFormatValue(s);
        } else {
            //说明时间小于1个小时,显示MMSSMM=分秒毫秒
            return getFormatValue(m) + connect + getFormatValue(s) + connect + getFormatValue(millis);
        }
    }

    public static String getFormatMMSS(long time, String connect) {
        long millis = time % 1000;
        long s = time / 1000 % 60;
        long m = time / (1000 * 60) % 60;
        return getFormatValue(m) + connect + getFormatValue(s);
    }

    private static String getFormatValue(long time) {
        String temp = time + "";
        if (temp.length() <= 1) {
            temp = "0" + temp;
        }
        return temp;
    }


    private static String getSecondString(long second) {
        String s = second + "";
        int length = s.length();
        if (length > 1) {
            return s;
        } else {
            return "0" + s;
        }
    }

    private static String getMinuteString(long minute) {
        String m = minute + "";
        int length = m.length();
        if (length > 1) {
            return m;
        } else {
            return "0" + m;
        }
    }

    private static String getHourString(long hour) {
        String h = hour + "";
        int length = h.length();
        if (length > 2) {
            return h;
        } else {
            if (hour == 0) {
                return "";
            } else {
                return "0" + h;
            }
        }
    }


}
