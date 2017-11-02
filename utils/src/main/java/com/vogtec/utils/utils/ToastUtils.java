package com.vogtec.utils.utils;

import android.content.Context;
import android.widget.Toast;

import com.vogtec.utils.Utils;

/**
 * Created by admin on 2016/8/31.
 * Toast工具类
 */
public abstract class ToastUtils {
    private static final Context CONTEXT = Utils.getContext();

    /**
     * 弹出Toast,短时间
     *
     * @param text 弹出的内容
     */
    public static void showToastShort(String text) {
        Toast toast = Toast.makeText(CONTEXT, text + "", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Toast日志
     *
     * @param text
     */
    public static void showToastShortForLog(String text) {
        if (Utils.TOAST_LOG) {
            Toast toast = Toast.makeText(CONTEXT, text + "", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 弹出Toast,短时间
     *
     * @param resId 弹出内容的资源ID
     */
    public static void showToastShort(int resId) {
        String toast = ResourceUtils.getString(resId);
        showToastShort(toast);
    }

    /**
     * 弹出Toast,短时间
     *
     * @param resId 弹出内容的资源ID
     */
    public static void showToastShortForLog(int resId) {
        if (Utils.TOAST_LOG) {
            String toast = ResourceUtils.getString(resId);
            showToastShort(toast);
        }
    }


    /**
     * 弹出Toast,长时间
     *
     * @param text
     */
    public static void showToastLong(String text) {
        Toast toast = Toast.makeText(CONTEXT, text + "", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 弹出Toast,长时间
     *
     * @param text
     */
    public static void showToastLongForLog(String text) {
        if (Utils.TOAST_LOG) {
            Toast toast = Toast.makeText(CONTEXT, text + "", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 弹出Toast,短时间
     *
     * @param resId
     */
    public static void showToastLong(int resId) {
        String toast = ResourceUtils.getString(resId);
        showToastLong(toast);
    }

    /**
     * 弹出Toast,短时间
     *
     * @param resId
     */
    public static void showToastLongForLog(int resId) {
        if (Utils.TOAST_LOG) {
            String toast = ResourceUtils.getString(resId);
            showToastLong(toast);
        }
    }

}
