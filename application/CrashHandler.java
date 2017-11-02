package com.vogtec.ibx5.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Process;

import com.tencent.bugly.crashreport.CrashReport;
import com.vogtec.ibx5.base.AppManager;
import com.vogtec.ibx5.manager.LogManager;

/**
 * Created by PC on 2016/12/21.
 * 崩溃捕捉
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static Object lock = new Object();
    private PendingIntent restartIntent;

    private CrashHandler() {
    }

    private static CrashHandler mCrashHandler;
    private Context mContext;

    public static CrashHandler getInstance() {
        synchronized (lock) {
            if (mCrashHandler == null) {
                synchronized (lock) {
                    if (mCrashHandler == null) {
                        mCrashHandler = new CrashHandler();
                    }
                }
            }
            return mCrashHandler;
        }
    }

    /* 初始化 */
    public void init(Context context) {
        this.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 以下用来捕获程序崩溃异常
        Intent intent = new Intent();
        // 参数1：包名，参数2：程序入口的activity
        intent.setClassName("com.vogtec.ibx5", "com.vogtec.ibx5.activity.CacheActivity");
        restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        ex.printStackTrace();
        LogManager.getInstance().printErrorLog(ex);//输出错误日志到SD卡中

        new Thread(new Runnable() {

            @Override
            public void run() {
                CrashReport.postCatchedException(ex);
                Looper.prepare();
                // 将Activity的栈清空
                AppManager.removeAll();
                restartApplication();
                // 退出程序
                Process.killProcess(Process.myPid());
            }
        }).start();
    }

    private void restartApplication() {
        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent);

    }
}
