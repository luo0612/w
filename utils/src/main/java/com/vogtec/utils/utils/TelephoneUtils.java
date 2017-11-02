package com.vogtec.utils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.vogtec.utils.Utils;

/**
 * Created by admin on 2016/8/31.
 * 手机工具类
 * 1.可以判断当前是否处于飞行模式
 */
public abstract class TelephoneUtils {
    private static final Context CONTEXT = Utils.getContext();

    /**
     * 判断手机当前是否处于飞行模式
     *
     * @return
     */
    public static boolean isAirolaneMode() {
        int isAirplaneMode = Settings.System.getInt(CONTEXT.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        return isAirplaneMode == 1;
    }

    /**
     * 获取Android设备唯一标识
     *
     * @return
     */
    public static String getAndroidId() {
        String androidId = Settings.Secure.getString(CONTEXT.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 判断是否连接到WIFI
     *
     * @return
     */
    public static boolean isWIFIConnected() {
        ConnectivityManager manager = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        } else {
            return false;
        }
    }
}
