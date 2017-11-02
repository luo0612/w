package com.vogtec.ibx5.application;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.vogtec.ibx5.db.MusicDBHelper;
import com.vogtec.ibx5.db.UserDBHelper;
import com.vogtec.ibx5.manager.FotaManager;
import com.vogtec.ibx5.manager.LogManager;
import com.vogtec.ibx5.utils.LogManagerUtils;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.LogUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by PC on 2016/12/6.
 * 应用入口
 */
public class BaseApplication extends Application {
    public static UserDBHelper helper;
    public static List<String> channels;
    public static MusicDBHelper musicDBHelper;
    //public static RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);*/
        Utils.TOAST_LOG = false;//显示Toast
        LogUtils.DEBUG = true;
        x.Ext.init(this);
        //FotaManager.getInstance().init(getApplicationContext());//Fota升级
        //FotaManager.getInstance().uploadDeviceInfo();
        CrashReport.initCrashReport(getApplicationContext(), "e0284dc46f", false);
        Utils.init(getApplicationContext());
        CrashHandler.getInstance().init(getApplicationContext());
        helper = UserDBHelper.getInstance(getApplicationContext());
        musicDBHelper = MusicDBHelper.getInstance(getApplicationContext());
        LogManager.getInstance();//Log线程池管理,初始化

    }

}
