package com.vogtec.ibx5.manager;

/**
 * Created by PC on 2017/3/15.
 */

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.vogtec.ibx5.domain.AccelerationSecurityAlgorithm;
import com.vogtec.ibx5.domain.SecurityAlgorithm;
import com.vogtec.ibx5.services.MusicService;
import com.vogtec.utils.Utils;
import com.vogtec.utils.domain.Music;
import com.vogtec.utils.utils.LogUtils;

import static android.content.Context.SENSOR_SERVICE;


/**
 * 安全管理器
 */
public class SecurityManager {
    private SecurityAlgorithm accelerationAlgorithm = new AccelerationSecurityAlgorithm();
    private static SecurityManager mManager = new SecurityManager();
    private boolean isSecurity = true;
    private LockManager mLockManager;
    private SensorManager mSensorManager;
    private final Sensor mAccelerometer;

    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            float[] values = event.values;
            switch (type) {
                case Sensor.TYPE_ACCELEROMETER:
                    accelerationChanged(values, event.timestamp);
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private SecurityManager() {
        mLockManager = LockManager.getInstance();
        mSensorManager = (SensorManager) Utils.getContext().getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static SecurityManager getInstance() {
        return mManager;
    }

    public void accelerationChanged(float[] values, long time, boolean isLock) {
        boolean security = accelerationAlgorithm.isSecurity(values, time);
        if (isLock) {
            //电子锁已经锁住
            setSecurity(security);
        } else {
            //电子锁没有锁住,关闭警报
            accelerationAlgorithm.reset();
            SpeakerManager.getInstance().stopSpeaker();
        }
    }

    public void accelerationChanged(float[] values, long time) {
        boolean security = accelerationAlgorithm.isSecurity(values, time);
        setSecurity(security);
    }

    private void setSecurity(boolean isSecurity) {
        if (this.isSecurity != isSecurity) {
            this.isSecurity = isSecurity;
            LogUtils.e(this, "isSecurity:" + isSecurity);
            if (!isSecurity) {
                AlarmManager.getInstance().startSpeaker();
            } else {
                AlarmManager.getInstance().stopSpeaker();
            }
        }
    }

    public boolean isSecurity() {
        return isSecurity;
    }

    public void reset() {
        accelerationAlgorithm.reset();

    }


    public void register() {
        mSensorManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        mSensorManager.unregisterListener(mSensorEventListener);
        AlarmManager.getInstance().stopSpeaker();
    }
}
