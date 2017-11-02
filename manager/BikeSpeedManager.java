package com.vogtec.ibx5.manager;

import com.vogtec.ibx5.base.Constants;
import com.vogtec.sensor.SpeedListener;
import com.vogtec.sensor.SpeedManager;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PC on 2017/4/20.
 * 速度管理
 */

public class BikeSpeedManager extends SpeedListener {
    private static final BikeSpeedManager MANAGER = new BikeSpeedManager();
    private final SpeedManager mSpeedManager;
    private List<OnSpeedChangeListener> mListeners = new ArrayList<OnSpeedChangeListener>();
    private int[] mSpeeds = new int[]{-1, -1, -1, -1, -1};
    public static final float WHEEL_LENGTH = 2.07F;//轮径周长

    private BikeSpeedManager() {
        mSpeedManager = (SpeedManager) Utils.getContext().getSystemService(Constants.SPEED_SERVICE);
        mSpeedManager.startListening(this);
    }

    public static BikeSpeedManager getInstance() {
        return MANAGER;
    }

    public interface OnSpeedChangeListener {
        void onSpeedChange(float speed);
    }

    /**
     * 添加速度的回调监听
     *
     * @param listener
     */
    public void addOnSpeedChangeListener(OnSpeedChangeListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    /**
     * 移除速度回调的监听
     *
     * @param listener
     */
    public void removeOnSpeedChangeListener(OnSpeedChangeListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void onMilliSecondPerCircle(int mspc) {
        super.onMilliSecondPerCircle(mspc);
        LogUtils.e(this, "speed:" + mspc);
        synchronized (BikeSpeedManager.class) {
            moveData();//移动数据
            mSpeeds[4] = mspc;
            LogUtils.e(this, "speeds:" + Arrays.toString(mSpeeds));
            if (isClutterData()) {
                //剔除杂乱数据
            } else {
                float currentSpeed = 0;
                if (mspc == 0) {
                    currentSpeed = 0;
                } else {
                    float distance = 1 * WHEEL_LENGTH;
                    float speedPerSec = distance / (mspc / 1000);//米/秒
                    currentSpeed = speedPerSec * 3.6f;
                }
                if (mListeners.size() > 0) {
                    for (OnSpeedChangeListener listener : mListeners) {
                        listener.onSpeedChange(currentSpeed);
                    }
                }
            }
        }
    }

    /**
     * 移动数据
     */
    private void moveData() {
        for (int i = 4; i >= 0; i--) {
            if (i > 0) {
                mSpeeds[i - 1] = mSpeeds[i];
                if (i == 4) {
                    mSpeeds[i] = -1;
                }
            }
        }
    }

    /**
     * 是否是杂乱的数据
     *
     * @return
     */
    private boolean isClutterData() {
        int speed0 = mSpeeds[0];
        int speed1 = mSpeeds[1];
        int speed2 = mSpeeds[2];
        int speed3 = mSpeeds[3];

        int speed4 = mSpeeds[4];

        if (speed0 == 0 && speed1 == 0 && speed2 == 0 && speed3 == 0 && speed4 > 0) {
            return true;
        }

        return false;
    }

    /**
     * 结束速度的监听
     */
    public void onDestroy() {
        mListeners.clear();
        if (mSpeedManager != null) {
            mSpeedManager.stopListening();
        }
    }

}
