package com.vogtec.ibx5.wifi;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by PC on 2017/3/8.
 */

public interface IWifiListener {
    void onAllWifi(List<ScanResult> ssids, String connectedSsid);

    void onWifiState(boolean state);
}
