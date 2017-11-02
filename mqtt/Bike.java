package com.vogtec.ibx5.mqtt;

/**
 * Created by PC on 2017/3/21.
 */

public class Bike {

    public Bike() {
    }

    public Bike(double latitude, double longitude, String bikeId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.bikeId = bikeId;
    }

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;
    /**
     * 自行车ID
     */
    private String bikeId;

    /**
     * 获取经度
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }
}
