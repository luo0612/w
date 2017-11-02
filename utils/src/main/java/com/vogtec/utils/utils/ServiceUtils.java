package com.vogtec.utils.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.vogtec.utils.Utils;

import java.util.List;

/**
 * Created by admin on 2016/8/31.
 */
public abstract class ServiceUtils {
    private static Context mContext = Utils.getContext();

    /**
     * 判断服务否运行
     *
     * @param name 服务Service的全路径名称
     * @return
     * @describe 判断服务是否在运行
     * @call
     */
    public boolean isRunning(String name) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfos) {
            String className = runningServiceInfo.service.getClassName();
            if (name.equals(className)) {
                return true;
            }
        }
        return false;
    }
}
