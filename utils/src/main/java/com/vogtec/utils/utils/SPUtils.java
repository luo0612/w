package com.vogtec.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vogtec.utils.Utils;

public abstract class SPUtils {
    private static Context mContext = Utils.getContext();
    // 配置文件的名称
    private static final String CONFIG = "vogtec_config";
    private static SharedPreferences mSP = mContext.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);


    /**
     * 获取config文件中key对应的值
     *
     * @param key      键名
     * @param defValue 当文件中不存在key时，返回的默认值
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return mSP.getBoolean(key, defValue);
    }

    /**
     * 向config文件中存储key-value键值数据
     *
     * @param key   键
     * @param value 值
     */
    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }



    /**
     * 获取config文件中key对应的值,当key不存在时,返回默认值defValue
     *
     * @param key      键
     * @param defValue 默认返回值
     * @return 值
     */
    public static String getString(String key, String defValue) {
        return mSP.getString(key, defValue);
    }

    /**
     * 向config文件中存储key-value键值数据
     *
     * @param key   键
     * @param value 值
     */
    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取config文件中key对应的值,当key不存在时,返回默认值defValue
     *
     * @param key      键
     * @param defValue 默认返回值
     * @return 值
     */
    public static int getInt(String key, int defValue) {
        return mSP.getInt(key, defValue);
    }

    /**
     * 向config文件中存储key-value键值数据
     *
     * @param key   键
     * @param value 值
     */
    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取config文件中key对应的值,当key不存在时,返回默认值defValue
     *
     * @param key      键
     * @param defValue 默认返回值
     * @return 值
     */
    public static float getFloat(String key, float defValue) {
        return mSP.getFloat(key, defValue);
    }

    /**
     * 向config文件中存储key-value键值数据
     *
     * @param key   键
     * @param value 值
     */
    public static void setFloat(String key, float value) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 获取config文件中key对应的值,当key不存在时,返回默认值defValue
     *
     * @param key      键
     * @param defValue 默认返回值
     * @return 值
     */
    public static long getLong(String key, long defValue) {
        return mSP.getLong(key, defValue);
    }

    /**
     * 向config文件中存储key-value键值数据
     *
     * @param key   键
     * @param value 值
     */
    public static void setLong(String key, long value) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clearInfo() {
        boolean commit = mSP.edit().clear().commit();
    }

}
