package com.vogtec.ibx5.wifi;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.vogtec.ibx5.R;
import com.vogtec.ibx5.base.BaseSwipeBackLayoutActivity;
import com.vogtec.utils.utils.ToastUtils;

/**
 * Created by PC on 2017/3/8.
 */

public class WifiPasswordActivity extends BaseSwipeBackLayoutActivity implements View.OnClickListener {

    private Button mBtConfirm;
    private WifiHelper mWifiHelper;
    private ImageView mIvBack;
    private EditText mEtPassword;

    @Override
    public void initView() {
        mBtConfirm = findView(R.id.bt_wifi_password_confirm);
        mIvBack = findView(R.id.iv_wifi_password_back);
        mEtPassword = findView(R.id.et_wifi_password_input);

    }

    @Override
    public void init() {
        mWifiHelper = new WifiHelper();
        mWifiHelper.startScan();
    }

    @Override
    public void initListener() {
        mBtConfirm.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.activity_wifi_password;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_wifi_password_back:
                finish();
                break;
            case R.id.bt_wifi_password_confirm:
                setWifiPassword();
                break;
        }
    }

    /**
     * 设置Wifi密码
     */
    private void setWifiPassword() {
        try {
            String password = mEtPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                ToastUtils.showToastShort(R.string.password_not_null);
                return;
            }
            mWifiHelper.connectConfiguration2(getIntent().getStringExtra("ssid"), password);
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToastShort(R.string.connect_failure);
        }
        setResult(1);
        finish();
    }
}
