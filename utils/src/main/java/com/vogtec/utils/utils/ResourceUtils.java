package com.vogtec.utils.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.vogtec.utils.Utils;

/**
 * Created by admin on 2016/8/31.
 * 资源工具类
 */
public abstract class ResourceUtils {
    private static Context mContext = Utils.getContext();

    /**
     * 通过资源id获取到颜色值,在应用中使用时,建议对其进行再次封装
     *
     * @param colorResId 颜色的资源id
     * @return
     */
    public static int getColor(int colorResId) {
        return mContext.getResources().getColor(colorResId);
    }

    /**
     * 通过资源id获取到图片对象,在应用中使用时,建议对其进行再次封装
     *
     * @param drawableResId 图片的资源id
     * @return
     */
    public static Drawable getDrawable(int drawableResId) {
        Drawable drawable = mContext.getResources().getDrawable(drawableResId);
        return drawable;
    }

    /**
     * 通过资源id获取到String对象,在应用中使用时,建议对其进行再次封装
     *
     * @param stringResId
     * @return
     * @describe 获取String资源文件的字段
     * @call
     */
    public static String getString(int stringResId) {
        return mContext.getResources().getString(stringResId);
    }

    /**
     * 通过资源id获取到String对象,并替换占位符,在应用中使用时,建议对其进行再次封装
     *
     * @param stringResId
     * @param obj
     * @return
     */
    public static String formatString(int stringResId, Object... obj) {
        String formatString = getString(stringResId);
        formatString = String.format(formatString, obj);
        return formatString;
    }

    /**
     * 获取String[]
     *
     * @param arrayResId
     * @return
     */
    public static String[] getStringArray(int arrayResId) {
        String[] array = mContext.getResources().getStringArray(arrayResId);
        return array;
    }

}
