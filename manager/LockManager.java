package com.vogtec.ibx5.manager;

import android.os.Handler;
import android.os.Looper;

import com.vogtec.core.BikeStatus;
import com.vogtec.ibx5.base.Constants;
import com.vogtec.ibx5.services.SecurityService;
import com.vogtec.security.ElectronicLockManager;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by PC on 2017/3/7.
 */

public class LockManager {
    private static LockManager manager = new LockManager();
    private final ElectronicLockManager lockManager;
    private ArrayList<OnLockListener> mLockListeners = new ArrayList<>();
    private LockStatusManagerListener mListener = new LockStatusManagerListener();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public interface OnLockListener {
        void onLock(int status);
    }

    private LockManager() {
        lockManager = (ElectronicLockManager) Utils.getContext().getSystemService(Constants.ELECTRONICLOCK_SERVICE);
    }

    public synchronized static LockManager getInstance() {
        return manager;
    }

    /**
     * 锁住
     */
    public void lock() {
        lockManager.lock();
    }

    /**
     * 解锁
     */
    public void unlock() {
        lockManager.unlock("1234567812345678");
    }

    public boolean isLock() {
        boolean lock = lockManager.isLock();
        return lock;
    }

    public void registerLock(OnLockListener onLockListener) {
        mLockListeners.add(onLockListener);
    }

    public void unregisterLock(OnLockListener onLockListener) {
        mLockListeners.remove(onLockListener);
    }

    /**
     * 设置锁的状态,true为锁住,false为false
     *
     * @param status
     */
    public void setLockStatus(boolean status) {
        if (status) {
            BikeStatusManagerContainer.getInstance().register(mListener);
            BikeStatusManagerContainer.getInstance().refreshStatus();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    BikeStatusManagerContainer.getInstance().refreshStatus();
                }
            }, 2000);

        } else {
            SecurityService.stopSecurityService();
        }
    }


    private class LockStatusManagerListener implements BikeStatusManagerContainer.BikeStatusManagerListener {

        @Override
        public void onChanged(BikeStatus status) {
            //LogFile.printLog(new Exception(status.lock + ""));
            //ToastUtils.showToastShort("安全服务自行车的状态:" + status.lock);
            LogUtils.e(this, "安全服务自行车的状态:" + status.lock);
            if (status.lock == 1) {
                SecurityService.startSecurityService();
            } else {
                //停止安全服务
                SecurityService.stopSecurityService();
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BikeStatusManagerContainer.getInstance().unregister(mListener);
                }
            }, 1000);
        }
    }

}
