package com.vogtec.utils.utils;

import android.os.Build;

/**
 * Created by admin on 2016/8/31.
 * 系统版本工具类
 */
public abstract class VersionUtils {
    /**
     * @return
     * @describe API大于等于8
     * @call
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;
    }

    /**
     * @return
     * @describe API大于等于9
     * @call
     */
    public static boolean hasGingerBread() {
        return Build.VERSION.SDK_INT >= 9;
    }

    /**
     * @return
     * @describe API大于等于11
     * @call
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11; // VERSION_CODES.HONEYCOMB;
    }

    /**
     * @return
     * @describe API大于等于12
     * @call
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12; // VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * @return
     * @describe API大于等于16
     * @call
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16; // VERSION_CODES.JELLY_BEAN;
    }

    /**
     * @return
     * @describe API大于等于19
     * @call
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19; // VERSION_CODES.KITKAT;
    }
}
