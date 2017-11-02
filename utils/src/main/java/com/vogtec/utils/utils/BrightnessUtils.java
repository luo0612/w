package com.vogtec.utils.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS;

/**
 * Android调节屏幕亮度工具类
 */
public class BrightnessUtils {


    /**
     * 判断是否开启自动亮度调节,只有开启自动亮度调节才能设置系统亮度
     *
     * @param context
     * @return
     */
    public static boolean IsAutoBrightness(Activity context) {
        boolean IsAutoBrightness = false;
        try {

            IsAutoBrightness =
                    (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return IsAutoBrightness;
    }


    /**
     * 获取当前系统屏幕的亮度
     *
     * @param context
     * @return
     */
    public static int getCurrentScreenBrightness(Activity context) {

        int nowBrightnessValue = 0;
        ContentResolver resolver = context.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }


    /**
     * 只设置当前Activity的亮度,退出当前Activity后,亮度无效
     *
     * @param context
     * @param brightness
     */
    public static void setCurWindowBrightness(Activity context, int brightness) {

        // 如果开启自动亮度，则关闭。否则，设置了亮度值也是无效的
        if (IsAutoBrightness(context)) {
            stopAutoBrightness(context);
        }

        // context转换为Activity
        Activity activity = (Activity) context;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

        // 异常处理
        if (brightness < 1) {
            brightness = 1;
        }

        // 异常处理
        if (brightness > 255) {
            brightness = 255;
        }

        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);

        activity.getWindow().setAttributes(lp);

    }


    /**
     * 设置系统屏幕的亮度,程序退出后依然有效
     *
     * @param context
     * @param brightness
     */
    public static void setSystemBrightness(Activity context, int brightness) {
        // 异常处理
        if (brightness < 1) {
            brightness = 1;
        }

        // 异常处理
        if (brightness > 255) {
            brightness = 255;
        }
        saveBrightness(context, brightness);
    }

    // 停止自动亮度调节

    public static void stopAutoBrightness(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    // 开启亮度自动调节

    public static void startAutoBrightness(Context context) {

        Settings.System.putInt(context.getContentResolver(),

                Settings.System.SCREEN_BRIGHTNESS_MODE,

                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

    }

    // 保存亮度设置状态

    public static void saveBrightness(Context context, int brightness) {

        Uri uri = android.provider.Settings.System.getUriFor(SCREEN_BRIGHTNESS);

        android.provider.Settings.System.putInt(context.getContentResolver(), SCREEN_BRIGHTNESS, brightness);

        context.getContentResolver().notifyChange(uri, null);
    }

}