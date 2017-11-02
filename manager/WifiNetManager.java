package com.vogtec.ibx5.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;

import com.vogtec.ibx5.wifi.IWifiListener;
import com.vogtec.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by PC on 2017/3/8.
 */

public class WifiNetManager {
    private static WifiNetManager manager = new WifiNetManager();
    private final WifiManager mWifiManager;
    private final CountDownTimer mTimer;
    private List<ScanResult> mSsids = new ArrayList<>();
    private String mConnectedSsid;
    private IWifiListener mIWifiListener;

    public void setIWifiListener(IWifiListener IWifiListener) {
        mIWifiListener = IWifiListener;
    }

    private WifiNetManager() {
        mWifiManager = (WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE);
        //开始扫描wifi
        mTimer = new CountDownTimer(Long.MAX_VALUE, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                refreshWifi();
                //开始扫描wifi
                mWifiManager.startScan();
            }

            @Override
            public void onFinish() {

            }
        };
    }

    /**
     * 刷新WIFI
     */
    private void refreshWifi() {
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        mConnectedSsid = mWifiManager.getConnectionInfo().getSSID();
        ConnectivityManager connManager = (ConnectivityManager) Utils.getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        allSsids(scanResults);

        if (mIWifiListener != null) {
            mIWifiListener.onAllWifi(scanResults, mConnectedSsid);
            mIWifiListener.onWifiState(mWifiManager.isWifiEnabled());
        }

    }

    /**
     * 添加所有的Wifi对象
     *
     * @param scanResults
     */
    private void allSsids(List<ScanResult> scanResults) {
        List<String> list = new ArrayList<String>();
        Iterator<ScanResult> iterator = scanResults.iterator();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            if (list.contains(next.SSID)) {
                iterator.remove();
            }
            list.add(next.SSID);
        }
        list.clear();
    }

    public List<ScanResult> allSsids() {
        return mSsids;
    }

    public static WifiNetManager getInstance() {
        return manager;
    }

    /**
     * 扫描wifi,只扫一次
     */
    public void scan() {
        mWifiManager.startScan();
        // SystemClock.sleep(500);
        //allSsids(mWifiManager.getScanResults());
        refreshWifi();
    }

    /**
     * 开始扫描WIFI,当WifiSetting界面打开的时候,手动的调用wifi扫描
     */
    public void startScan() {
        mTimer.start();
    }

    /**
     * 结束扫描WIFI
     */
    public void stopScan() {
        mTimer.cancel();
    }

    /**
     * 打开wifi
     */
    public void openWifi() {
        mWifiManager.setWifiEnabled(true);
    }

    public boolean isOpen() {
        boolean isOpen = false;
        switch (mWifiManager.getWifiState()) {
            // switch判断状态
            case WifiManager.WIFI_STATE_DISABLED:
            case WifiManager.WIFI_STATE_DISABLING:
            case WifiManager.WIFI_STATE_ENABLING:
            case WifiManager.WIFI_STATE_UNKNOWN:
                isOpen = false;
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                isOpen = true;
                break;
        }
        return isOpen;
    }

    /**
     * 关闭wifi
     */
    public void closeWifi() {
        mWifiManager.setWifiEnabled(false);
    }
}
