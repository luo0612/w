package com.vogtec.ibx5.wifi;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vogtec.ibx5.R;
import com.vogtec.ibx5.base.BaseSwipeBackLayoutActivity;
import com.vogtec.utils.utils.ResourceUtils;
import com.vogtec.utils.utils.UIUtils;

import java.util.List;

public class WifiSettingActivity extends BaseSwipeBackLayoutActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView mIvBack;
    private static final String CAP_ESS = "[ESS]";
    private IWifiListener mIWifiListener = new IWifiListener() {
        @Override
        public void onAllWifi(List<ScanResult> ssids, String connectedSsid) {
            setShowWifi(ssids, connectedSsid);
        }


        @Override
        public void onWifiState(boolean state) {
            // mCbState.setChecked(state);
        }
    };
    private LinearLayout mLlCurrentWifi;
    private ConnectivityManager mConnManager;

    private void setShowWifi(List<ScanResult> scanResults, String connectedSsid) {
        mLlContainer.removeAllViews();
        boolean currentWifi = false;
        int index = 0;
        for (ScanResult scanResult : scanResults) {
            View child = UIUtils.inflate(R.layout.item_wifi);
            TextView tvSsid = (TextView) child.findViewById(R.id.tv_item_wifi_ssid);
            tvSsid.setText(scanResult.SSID);

            NetworkInfo wifiInfo = mConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!TextUtils.isEmpty(connectedSsid) && connectedSsid.contains(scanResult.SSID) && wifiInfo.isConnected()) {
                tvSsid.setTextColor(ResourceUtils.getColor(R.color.main_color));
                currentWifi = true;
                mTvCurrentWifi.setText(scanResult.SSID);
            } else {
                tvSsid.setTextColor(ResourceUtils.getColor(R.color._FFFFFF));
            }

            ImageView lock = (ImageView) child.findViewById(R.id.iv_wifi_item_lock);
            if (CAP_ESS.equals(scanResult.capabilities)) {
                lock.setImageResource(R.mipmap.m_wifi_unlock_w);
            } else {
                lock.setImageResource(R.mipmap.m_wifi_lock_w);
            }

            mLlContainer.addView(child, index);

            child.setId(index);
            child.setOnClickListener(onWifiItemClickListener);
            child.setTag(scanResult);

            index++;
        }
        if (currentWifi) {
            mLlCurrentWifi.setVisibility(View.VISIBLE);
        } else {
            mLlCurrentWifi.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener onWifiItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ScanResult tag = (ScanResult) v.getTag();
            Intent intent = new Intent(WifiSettingActivity.this, WifiPasswordActivity.class);
            intent.putExtra("ssid", tag.SSID);
            startActivityForResult(intent, 0);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                mLlCurrentWifi.setVisibility(View.VISIBLE);
            } else if (resultCode == 1) {
                mLlCurrentWifi.setVisibility(View.GONE);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWifiBinder = (WifiService.WifiBinder) service;
            mWifiBinder.registerWifiListener(mIWifiListener);
            mCbState.setChecked(mWifiBinder.getWifiState());
            mWifiBinder.startScan();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWifiBinder.stopScan();
            mWifiBinder.unregisterWifiLisener();
        }
    };
    private WifiService.WifiBinder mWifiBinder;
    private CheckBox mCbState;
    private TextView mTvCurrentWifi;
    private LinearLayout mLlContainer;

    @Override
    public void initView() {
        mIvBack = findView(R.id.iv_wifi_back);
        mCbState = findView(R.id.cb_wifi_switch);
        mTvCurrentWifi = findView(R.id.tv_wifi_current_wifi);
        mLlContainer = findView(R.id.ll_wifi_list);

        mLlCurrentWifi = findView(R.id.ll_wifi_current);
    }

    @Override
    public void init() {
        bindWifiService();
        initNetwork();
    }

    private void initNetwork() {
        mConnManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

    }

    private void bindWifiService() {
        Intent intent = new Intent(this, WifiService.class);
        bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mCbState.setOnCheckedChangeListener(this);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.activity_wifi_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_wifi_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    /**
     * 控制WIFI的关闭
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mWifiBinder != null) {
            mWifiBinder.oppenWifi(isChecked);
            mWifiBinder.refreshWifi();
        }
    }
}
