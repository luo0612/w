package com.vogtec.utils.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;

/**
 * Created by admin on 2016/9/14.
 * 蓝牙工具类
 */
public class BluetoothUtils {
    /**
     * 判断设备蓝牙的状态
     *
     * @param adapter
     * @return true:可用  false:不可用
     */
    public static boolean getBluetoothState(BluetoothAdapter adapter) {
        if (adapter == null) {
            return false;
        } else if (!adapter.isEnabled()) {
            return false;
        } else if (adapter.isEnabled()) {
            //可操控蓝牙设备
            int a2dp = adapter.getProfileConnectionState(BluetoothProfile.A2DP);
            //头戴式耳机设备
            int headset = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);
            //穿戴式设备
            int health = adapter.getProfileConnectionState(BluetoothProfile.HEALTH);

            if (a2dp == BluetoothProfile.STATE_CONNECTED ||
                    headset == BluetoothProfile.STATE_CONNECTED ||
                    health == BluetoothProfile.STATE_CONNECTED) {
                return false;
            }

            boolean bluetoothConnected = NetUtils.isBluetoothConnected();
            if (bluetoothConnected) {
                return false;
            }
        }
        return true;
    }
}
