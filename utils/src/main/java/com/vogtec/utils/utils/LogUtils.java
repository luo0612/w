package com.vogtec.utils.utils;

import android.util.Log;

/**
 * Created by admin on 2016/8/31.
 * 日志工具类
 */
public abstract class LogUtils {
    // 是否输出日志的标记
    public static boolean DEBUG = true;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;

    private static int mLevel = LEVEL_VERBOSE;

    /**
     * Log.v()
     */
    public static void v(Object obj, String msg) {
        // 将获取obj的类名
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_VERBOSE) {
            Log.v(tag, msg + "");
        }
    }

    public static void s(Object obj, String msg) {
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_VERBOSE) {
            System.out.println(msg);
        }
    }

    /**
     * Log.d()
     */
    public static void d(Object obj, String msg) {
        // 将获取obj的类名
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_DEBUG) {
            Log.d(tag, msg + "");
        }
    }

    public static void d(Object obj, String msg, Object... objs) {

        if (mLevel > LEVEL_DEBUG) {
            return;
        }

        String tag = getClassName(obj);
        if (objs.length > 0) {
            msg = String.format(msg, objs);
        }
        Log.d(tag, msg);
    }

    /**
     * Log.i()
     */
    public static void i(Object obj, String msg) {
        // 将获取obj的类名
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_INFO) {
            Log.i(tag, msg + "");
        }
    }

    /**
     * Log.w()
     */
    public static void w(Object obj, String message) {
        // 将获取obj的类名
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_WARNING) {
            Log.w(tag, message + "");
        }
    }

    /**
     * Log.e()
     */
    public static void e(Object obj, String message) {
        // 将获取obj的类名
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_ERROR) {
            Log.e(tag, message + "");
        }
    }

    /**
     * Log.e() 异常信息打印
     */
    public static void e(Object obj, String message, Throwable e) {
        // 将获取obj的类名
        String tag = getClassName(obj);
        if (mLevel <= LEVEL_ERROR) {
            Log.e(tag, message + "", e);
        }
    }

    /**
     * 获取obj对象类名的方法
     *
     * @return obj的简单类名
     */
    private static String getClassName(Object obj) {
        String s = "";

        if (obj instanceof Class) {
            Class clazz = (Class) obj;
            s = clazz.getSimpleName();
        } else if (obj instanceof String) {
            String str = (String) obj;
            s = str;
        } else {
            s = obj.getClass().getSimpleName();
        }
        return s;
    }

}
