package com.vogtec.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.vogtec.utils.Utils;

import java.io.File;

/**
 * Created by admin on 2016/8/31.
 * 界面工具类
 */
public abstract class UIUtils {
    private static final Context CONTEXT = Utils.getContext();

     /**
     * 触摸隐藏软键盘
     *
     * @param view
     * @param activity
     */
    public static void onTouchHideSoftKeyBoard(View view, final Activity activity) {
        if (view != null && activity != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 打开系统相册,图片地址的获取通过activity的onActivityResult()方法获取
     *
     * @param activity
     * @param requestCode
     */
    public static void getPhotoFromSystemPhoto(Activity activity, int requestCode) {
        Intent intent;
        //如果系统版本大于19
        if (VersionUtils.hasKitKat()) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 通过系统相机获取图片,图片的地址为photoPath,在activity的onActivityResult()方法中获取,
     * 因为有可能onActivityResult()方法中返回的数据可能为null,因此需要传入图片保存的地址
     *
     * @param activity
     * @param photoPath   图片保存的路径
     * @param requestCode
     */
    public static void getPhotoFromCamera(Activity activity, String photoPath, int requestCode) {
        File file = new File(photoPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 将XML布局转换成View对象
     *
     * @param layoutResId
     * @return
     */
    public static View inflate(int layoutResId) {
        View view = View.inflate(CONTEXT, layoutResId, null);
        return view;
    }

    public static int getAppVersionCode() {
        PackageManager manager = CONTEXT.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(CONTEXT.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
