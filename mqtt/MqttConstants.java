package com.vogtec.ibx5.mqtt;

import android.text.TextUtils;

import com.vogtec.ibx5.base.Api;
import com.vogtec.ibx5.base.BikeState;
import com.vogtec.ibx5.base.Constants;
import com.vogtec.ibx5.utils.LogFile;
import com.vogtec.utils.utils.SPUtils;


/**
 * Created by PC on 2017/3/22.
 */

public class MqttConstants {

    public static String MQTT_HOST;

    static {
        if (Api.debug) {
            String mqttIp = SPUtils.getString(Constants.MQTT_IP, "");
            if (!TextUtils.isEmpty(mqttIp)) {
                MQTT_HOST = mqttIp;
            } else {
                //测试环境
                MQTT_HOST = "tcp://118.190.0.209:11883";
            }
        } else {
            String mqttIp = SPUtils.getString(Constants.MQTT_IP, "");
            if (!TextUtils.isEmpty(mqttIp)) {
                MQTT_HOST = mqttIp;
            } else {
                //正式环境
                MQTT_HOST = "tcp://116.62.187.168:1183";
            }

        }
    }

    private String CLIENT_ID;
    private String USERNAME;
    private String PASSWORD = "123456";
    private static MqttConstants mqttConstants = new MqttConstants();
    private String IMEI;
    private String BIKE_NUMBER;
    public static String IMEI_FOR_UUID = null;

    private MqttConstants() {
    }


    public static MqttConstants getInstance() {
        return mqttConstants;
    }

    /**
     * 获取服务器主机
     *
     * @return
     */
    public String getMqttHost() {
        return MQTT_HOST;
    }

    /**
     * 获取客户端的ID
     *
     * @return
     */
    public String getClientId() {
        if (TextUtils.isEmpty(CLIENT_ID)) {
            CLIENT_ID = SPUtils.getString(Constants.CLIENT_ID, "");
        }
        return CLIENT_ID;
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getUsername() {
        if (TextUtils.isEmpty(USERNAME)) {
            USERNAME = SPUtils.getString(Constants.USERNAME, "");
        }
        return USERNAME;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return PASSWORD;
    }

    /**
     * 设置密码
     *
     * @param password
     */
    public void setPassword(String password) {
        PASSWORD = password;
    }

    /**
     * 获取IMEI号
     *
     * @return
     */
    public String getIMEI() {
        if (TextUtils.isEmpty(IMEI)) {
            IMEI = SPUtils.getString(Constants.IMEI_FOR_UUID, "");
        }
        return IMEI;
    }

    public String getBikeNumber() {
        if (TextUtils.isEmpty(BIKE_NUMBER)) {
            BIKE_NUMBER = SPUtils.getString(Constants.BIKE_NUMBER, "");
        }
        return BIKE_NUMBER;
    }

    /**
     * 设置车牌号
     *
     * @param bikeNumber
     */
    public void setBikeNumber(String bikeNumber) {

        BIKE_NUMBER = bikeNumber;
        USERNAME = "bike:" + BIKE_NUMBER;

        SPUtils.setString(Constants.BIKE_NUMBER, BIKE_NUMBER);
        SPUtils.setString(Constants.USERNAME, USERNAME);

        BikeState.getInstance().setBikeNumber(BIKE_NUMBER);
        BikeState.getInstance().setUsername(USERNAME);
    }

    public void setImei(String deviceId) {
        IMEI = deviceId;
        CLIENT_ID = "bike:" + IMEI;
        PASSWORD = "123456";

        //设置自行车车牌号和用户名
        setBikeNumberAndUsername();

        SPUtils.setString(Constants.IMEI_FOR_UUID, IMEI);//将IMEI保存到SP中
        SPUtils.setString(Constants.CLIENT_ID, CLIENT_ID);
        SPUtils.setString(Constants.PASSWORD, PASSWORD);

        LogFile.printImeiForUUID(IMEI);//保存到SDCard中

        BikeState.getInstance().setImei(IMEI);//将IMEI设置到自行车状态BikeState中
        BikeState.getInstance().setClientId(CLIENT_ID);//将ClientId设置到自行车的状态BikeState中
        BikeState.getInstance().setPassword(PASSWORD);//将Password设置到自行车的状态BikeState中
    }

    private void setBikeNumberAndUsername() {
        BIKE_NUMBER = SPUtils.getString(Constants.BIKE_NUMBER, "");

        if (!TextUtils.isEmpty(BIKE_NUMBER)) {
            //将BikeNumber设置到自行车的状态BikeState中
            BikeState.getInstance().setBikeNumber(BIKE_NUMBER);

            USERNAME = "bike:" + BIKE_NUMBER;
            //将Username设置到自行车的状态BikeState中
            BikeState.getInstance().setUsername(USERNAME);
        }
    }
}

