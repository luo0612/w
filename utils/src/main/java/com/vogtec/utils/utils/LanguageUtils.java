package com.vogtec.utils.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.vogtec.utils.Utils;

import java.util.Locale;

/**
 * Created by PC on 2016/12/16.
 */
public abstract class LanguageUtils {
    private static Context mContext = Utils.getContext();

    /**
     * 设置系统时间
     *
     * @param languageSimpleCode
     */
    public static void setSelectLanguage(String languageSimpleCode) {
        Resources resources = mContext.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = new Locale(languageSimpleCode); // 设置语言
        resources.updateConfiguration(config, dm);
    }
}
