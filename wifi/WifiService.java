package com.vogtec.ibx5.wifi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.vogtec.ibx5.manager.WifiNetManager;

/**
 * Created by PC on 2017/3/8.
 */

public class WifiService extends Service {

    private WifiNetManager mWifiNetManager;
    private WifiBinder mWifiBinder;

    public class WifiBinder extends Binder {
        /**
         * 注册wifi监听
         *
         * @param iWifiListener
         */
        public void registerWifiListener(IWifiListener iWifiListener) {
            mWifiNetManager.setIWifiListener(iWifiListener);
        }
        /**
         * 解除注册wifi监听
         */
        public void unregisterWifiLisener() {
            mWifiNetManager.setIWifiListener(null);
        }

        /**
         * 开始扫描
         */
        public void startScan() {
            mWifiNetManager.startScan();
        }

        /**
         * 结束扫描
         */
        public void stopScan() {
            mWifiNetManager.stopScan();
        }


        public void oppenWifi(boolean isOpen) {
            if (isOpen) {
                mWifiNetManager.openWifi();
            } else {
                mWifiNetManager.closeWifi();
            }
        }

        public boolean getWifiState() {
            return mWifiNetManager.isOpen();
        }

        /**
         * 刷新
         */
        public void refreshWifi() {
            mWifiNetManager.startScan();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent innt) {
        mWifiBinder = new WifiBinder();
        return mWifiBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiNetManager = WifiNetManager.getInstance();
        boolean open = mWifiNetManager.isOpen();
        if (!open) {
            mWifiNetManager.openWifi();
        }
        mWifiNetManager.scan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWifiNetManager.closeWifi();
    }
}
