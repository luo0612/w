package com.vogtec.ibx5.manager;

import com.vogtec.ibx5.base.BaseActivity;

import java.util.Stack;

/**
 * Created by PC on 2017/3/7.
 */

public class ActivityManager {
    private static Stack<BaseActivity> activityStack;
    private static ActivityManager instance = new ActivityManager();

    private ActivityManager() {
        activityStack = new Stack<BaseActivity>();
    }

    public static ActivityManager getInstance() {

        return instance;
    }

    public void popActivity() {
        BaseActivity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(BaseActivity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public BaseActivity get() {
        BaseActivity baseActivity = activityStack.get(activityStack.size() - 2);
        return baseActivity;
    }

    public BaseActivity currentActivity() {
        BaseActivity activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(BaseActivity activity) {
        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            BaseActivity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }
}
