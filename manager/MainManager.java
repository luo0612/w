package com.vogtec.ibx5.manager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.vogtec.ibx5.base.BaseActivity;
import com.vogtec.utils.Utils;

/**
 * Created by PC on 2017/3/14.
 */

public class MainManager {
    /**
     * SettingFragment加载数据
     */
    public static final String ACTION_SETTING_LOAD_DATA = "com.vogtec.action.SETTING_LOAD_DATA";
    /**
     * 改变语言
     */
    public static final String ACTION_CHANGE_LANGUAGE = "com.vogtec.action.CHANGE_LANGUAGE";
    /**
     * 刷新时间
     */
    public static final String ACTION_REFRESH_TIME = "com.vogtec.action.REFRESH_TIME";

    /**
     * 初始化SettingFragment
     */
    public static final String ACTION_SETTING_INIT = "com.vogtec.action.SETTING_INIT";

    /**
     * 关闭MainActivity
     */
    public static final String ACTION_FINISH = "com.vogtec.action.FINISH";

    private static MainManager manager = new MainManager();

    private MainManager() {
    }

    public static synchronized MainManager getInstance() {
        return manager;
    }

    /**
     * SettingFragment加载数据
     */
    public void loadDataForSetting() {
        sendBroadcast(ACTION_SETTING_LOAD_DATA);
    }

    /**
     * 改变语言
     */
    public void changeLanguage() {
        sendBroadcast(ACTION_CHANGE_LANGUAGE);
    }

    /**
     * 刷新时间
     */
    public void refreshTime() {
        sendBroadcast(ACTION_REFRESH_TIME);
    }

    /**
     * 初始化SettingFragment
     */
    public void initSetting() {
        sendBroadcast(ACTION_SETTING_INIT);
    }

    /**
     * 关闭MainActivity
     */
    public void finish() {
        sendBroadcast(ACTION_FINISH);
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        Utils.getContext().sendBroadcast(intent);
    }

    /**
     * 注册监听
     *
     * @param receiver
     * @param activity
     * @param action
     */
    public static void register(BroadcastReceiver receiver, BaseActivity activity, String action) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        activity.registerReceiver(receiver, intentFilter);
    }

    /**
     * 解除注册监听
     *
     * @param receiver
     * @param activity
     */
    public static void unregister(BroadcastReceiver receiver, BaseActivity activity) {
        activity.unregisterReceiver(receiver);
    }
}
