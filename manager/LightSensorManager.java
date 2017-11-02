package com.vogtec.ibx5.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.vogtec.ibx5.base.Constants;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.SPUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.android.internal.widget.TextProgressBar.TAG;

/**
 * Created by PC on 2017/3/9.
 * 光线传感器管理者
 */

public class LightSensorManager {

    private static LightSensorManager manager = new LightSensorManager();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long firstNightTime = Long.MAX_VALUE;
    private static final long OPEN_INTERVAL = TimeUnit.SECONDS.toNanos(4);
    private LightManager mLightManager;

    private LightSensorManager() {
        init();
    }

    public static LightSensorManager getInstance() {
        return manager;
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            boolean auto = SPUtils.getBoolean(Constants.AUTO_LIGHT, true);
            //获取光线强度
            float light = event.values[0];
            if (isNight(light)) {//判断是否小于阈值
                if (isTimeNight()) {
                    //位于晚上,打开所有的灯光
                    mLightManager.openAll();
                } else {
                    //位于白天
                    if (firstNightTime == Long.MAX_VALUE) {
                        firstNightTime = event.timestamp;
                    }
                    if (event.timestamp - firstNightTime > OPEN_INTERVAL) {
                        //判断两次(光线强度小于阈值)的时间差大于指定的时间间隔值时,打开所有的灯光
                        mLightManager.openAll();
                        Log.i(TAG, "light:openAll");
                    } else {
                        Log.i(TAG, "light:may be press back button");
                    }
                }
            } else {
                //关闭所有的灯光
                mLightManager.closeAll();
                firstNightTime = Long.MAX_VALUE;
                Log.i(TAG, "light:closeAll");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void init() {
        mSensorManager = (SensorManager) Utils.getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mLightManager = LightManager.getInstance();
    }

    public void register() {
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mLightManager.startListener();
    }

    public void unregister() {
        //防止在自动照明的过程中,关闭自动照明,灯光不熄灭的问题
        mLightManager.closeAll();
        
        mSensorManager.unregisterListener(mSensorEventListener, mSensor);
        mLightManager.stopListener();
    }

    /**
     * 判断是否亮灯
     *
     * @param light
     * @return
     */
    private boolean isNight(float light) {
        return light <= SensorManager.LIGHT_FULLMOON;
        //return light <= 2f;
    }

    /**
     * 判断是否是白天
     *
     * @return
     */
    private boolean isTimeNight() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 18 || hour <= 7) {
            return true;
        }
        return false;
    }


}
