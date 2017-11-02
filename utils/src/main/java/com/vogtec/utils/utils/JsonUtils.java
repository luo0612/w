package com.vogtec.utils.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

/**
 * Created by admin on 2016/8/31.
 * Json工具类
 */
public abstract class JsonUtils {
    private static final String GOSN_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static Gson mGson = new Gson();
    private static Gson gosn = new GsonBuilder().setDateFormat(GOSN_FORMAT).create();

    /**
     * 获取Gson对象
     *
     * @return
     */
    public static Gson getGson() {
        return mGson;
    }

    /**
     * 将对象转换成json字符串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objectToJson(T obj) {
        return mGson.toJson(obj);
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return mGson.fromJson(json, clazz);
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(JsonObject json, Class<T> clazz) {
        return mGson.fromJson(json, clazz);
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Type type) {
        return mGson.fromJson(json, type);
    }

}
