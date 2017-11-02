package com.vogtec.ibx5.manager;

/**
 * Created by PC on 2017/4/17.
 */

import android.os.Environment;

import com.vogtec.ibx5.log.MqttTask;
import com.vogtec.ibx5.log.ThreadPoolManager;
import com.vogtec.utils.utils.FileUtils;
import com.vogtec.utils.utils.LogUtils;

import java.io.File;

/**
 * 本地日志管理
 */
public class LogManager {
    private static final LogManager MANAGER = new LogManager();
    private ThreadPoolManager mThreadPoolManager;
    /**
     * 错误日志的文件
     */
    private static final String ERROR_LOG_FILE_PATH;
    /**
     * Log信息的文件
     */
    private static final String LOG_INFO_FILE_PATH;

    /**
     * Mqtt日志的文件
     */
    private static final String MQTT_LOG_FILE_PATH;

    /**
     * 定位日志文件的路径
     */
    private static final String LOATION_LOG_FILE_PATH;


    static {
        ERROR_LOG_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "error_log.txt";
        LOG_INFO_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "log_info.txt";
        MQTT_LOG_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "mqtt_log.txt";
        LOATION_LOG_FILE_PATH = Environment.getDownloadCacheDirectory() + File.separator + "location_log.txt";
    }

    public static File errorLogFile = new File(ERROR_LOG_FILE_PATH);
    public static File logInfoFile = new File(LOG_INFO_FILE_PATH);
    public static File mqttLogFile = new File(MQTT_LOG_FILE_PATH);
    public static File locationLogFile = new File(LOATION_LOG_FILE_PATH);

    private LogManager() {
        init();
    }

    private void init() {
        mThreadPoolManager = ThreadPoolManager.getInstance();
    }

    public static final LogManager getInstance() {
        return MANAGER;
    }

    private LogManager checkLocationLogFile() {
        if (!locationLogFile.exists()) {
            return MANAGER;
        } else {
            double size = FileUtils.getFileOrDirectorySize(LOATION_LOG_FILE_PATH, FileUtils.SIZETYPE_MB);
            if (size > 50) {
                locationLogFile.delete();
                locationLogFile = null;
                locationLogFile = new File(LOATION_LOG_FILE_PATH);
            }
        }
        return MANAGER;
    }

    /**
     * 校验日志文件的大小,如果日志文件太大,删除该日志文件
     *
     * @return
     */
    private LogManager checkErrorLogFile() {
        if (!errorLogFile.exists()) {
            return MANAGER;
        } else {
            double size = FileUtils.getFileOrDirectorySize(ERROR_LOG_FILE_PATH, FileUtils.SIZETYPE_MB);
            if (size > 50) {
                synchronized (LogManager.class) {
                    errorLogFile.delete();
                    errorLogFile = null;
                    errorLogFile = new File(ERROR_LOG_FILE_PATH);
                }
            }
        }
        return MANAGER;
    }

    private LogManager checkMqttLogFile() {
        if (!mqttLogFile.exists()) {
            return MANAGER;
        } else {
            double size = FileUtils.getFileOrDirectorySize(MQTT_LOG_FILE_PATH, FileUtils.SIZETYPE_MB);
            if (size > 50) {
                synchronized (MqttTask.class) {
                    mqttLogFile.delete();
                    mqttLogFile = null;
                    mqttLogFile = new File(MQTT_LOG_FILE_PATH);
                }
            }
        }
        return MANAGER;
    }

    private LogManager checkLogInfoFile() {
        if (!logInfoFile.exists()) {
            return MANAGER;
        } else {
            double size = FileUtils.getFileOrDirectorySize(LOG_INFO_FILE_PATH, FileUtils.SIZETYPE_MB);
            if (size > 50) {
                synchronized (Object.class) {
                    logInfoFile.delete();
                    logInfoFile = null;
                    logInfoFile = new File(LOG_INFO_FILE_PATH);
                }
            }
        }
        return MANAGER;
    }

    /**
     * 打印普通Log信息
     *
     * @param log
     */
    public void printLog(String log) {
        if (LogUtils.DEBUG) {
            checkLogInfoFile();
            mThreadPoolManager.addLogMsg(log);
        }
    }

    /**
     * 打印异常的Log信息
     *
     * @param log
     */
    public void printErrorLog(Throwable log) {
        if (LogUtils.DEBUG) {
            checkErrorLogFile();
            mThreadPoolManager.addErrorLogMsg(log);
        }
    }

    /**
     * 打印Mqtt的Log信息
     *
     * @param log
     */
    public void printMqttLog(String log) {
        if (LogUtils.DEBUG) {
            checkMqttLogFile();
            mThreadPoolManager.addMqttLogMsg(log);
        }
    }

    /**
     * 打印定位日志
     *
     * @param log
     */
    public void printLocationLog(String log) {
        if (LogUtils.DEBUG) {
            checkLocationLogFile();
            mThreadPoolManager.addLocationMsg(log);
        }
    }
}
