package com.vogtec.ibx5.mqtt;

/**
 * Created by PC on 2017/3/21.
 */

/**
 * 关于锁车的相关常量字段
 */
public interface LockConstants {

    /**
     * 自行车的Token
     */
    String BIKE_TOKEN = "bike_token";

    /**
     * 确认开锁
     */
    //vogtec/gx/unlock/ack
    String CONFIRM_UNLOCK = "vogtec/gx/unlock/ack";

    /**
     * 锁车上报
     */
    String PUBLISH_LOCK = "vogtec/gx/lock";

    /**
     * GPS定位的主题
     */
    String BIKE_GPS = "vogtec/gx/gps/report";

    /**
     * 电量上报的主题
     */
    String BIKE_BATTERY = "vogtec/gx/bat";

    /**
     * 高德地图GPS定位上报
     */
    String BIKE_GPS_AMAP_REPORT = "vogtec/gx/amap/report";

    /**
     * 锁车补传
     */
    String BIKE_LOCK_ADD = "vogtec/gx/lock/add";
}
