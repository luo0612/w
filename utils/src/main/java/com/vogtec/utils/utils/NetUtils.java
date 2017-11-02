package com.vogtec.utils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vogtec.utils.Utils;

/**
 * Created by admin on 2016/8/31.
 * 网络连接状态工具类
 */
public abstract class NetUtils {
    private static final Context CONTEXT = Utils.getContext();

    /**
     * 网络类型:没有网络
     */
    public static final int TYPE_NO = -1;
    /**
     * 网络类型:WIFI网络
     */
    public static final int TYPE_WIFI = 1;
    /**
     * 网络类型:WAP网络
     */
    public static final int TYPE_WAP = 2;
    /**
     * 网络类型:NET网络
     */
    public static final int TYPE_NET = 3;
    /**
     * 网络类型:蓝牙
     */
    public static final int TYPE_BLUETOOTH = 4;

    /**
     * 判读是否连接到网络
     *
     * @return
     */
    public static boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return true;
        } else {
            return false;
        }
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

    /**
     * 判断是否连接到移动网络
     *
     * @return
     */
    public static boolean isMobileConnected() {
        ConnectivityManager manager = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        } else {
            return false;
        }
    }

    /**
     * 判断是否连接到蓝牙网络
     *
     * @return
     */
    public static boolean isBluetoothConnected() {
        ConnectivityManager manager = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        } else {
            return false;
        }
    }

    /**
     * 获取网络类型
     * <p>
     * TYPE_NO:没有网络
     * TYPE_WIFI:WIFI网络
     * TYPE_WAP:WAP网络
     * TYPE_NET:NET网络
     * TYPE_BLUETOOTH:蓝牙
     *
     * @return
     */
    public static int getNetworkType() {
        ConnectivityManager manager = (ConnectivityManager) CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return TYPE_NO;
        }
        int type = activeNetworkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            if (activeNetworkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return TYPE_NET;
            } else {
                return TYPE_WAP;
            }
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return TYPE_WIFI;
        } else if (type == ConnectivityManager.TYPE_BLUETOOTH) {
            return TYPE_BLUETOOTH;
        }
        return TYPE_NO;
    }


}
