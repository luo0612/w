package com.vogtec.utils;

import android.content.Context;

/**
 * Created by admin on 2016/8/31.
 * 工具类,在使用utils的包中的类时,需要先对此类进行初始化
 */
public abstract class Utils {
    public static boolean TOAST_LOG = true;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public synchronized static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("Utils类没有进行初始化,请在Application中进行初始化");
        }
        return mContext;
    }
}
