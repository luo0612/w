package com.vogtec.ibx5.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.redstone.ota.callback.IConfigCallback;
import com.redstone.ota.callback.IDeviceInfoCallback;
import com.redstone.ota.callback.RsCheckRequestCallback;
import com.redstone.ota.callback.RsDownloadRequestCallback;
import com.redstone.ota.main.RsFwUpdatePackage;
import com.redstone.ota.main.RsOtaAgent;
import com.vogtec.ibx5.R;
import com.vogtec.ibx5.activity.MainActivity;
import com.vogtec.ibx5.utils.LogFile;
import com.vogtec.utils.utils.FileUtils;
import com.vogtec.utils.utils.ResourceUtils;

/**
 * Created by PC on 2017/5/18.
 * Fota升级管理者
 */

public class FotaManager {
    private static final FotaManager MANAGER = new FotaManager();

    private Context mContext;
    private TelephonyManager mTelephonyManager;
    private ActivityManager mMyActivityManager;

    private FotaManager() {
    }

    public static FotaManager getInstance() {
        return MANAGER;
    }

    public void init(Context context) {
        mContext = context;
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mMyActivityManager = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
        RsOtaAgent.getInstance().init(context);
    }

    /**
     * 上传设备信息
     */
    public void uploadDeviceInfo() {
        RsOtaAgent.getInstance().setDeviceInfo(new DeviceInfoCallback());
        RsOtaAgent.getInstance().setUpdateStrategy(new ConfigCallback());
    }


    /**
     * 检测更新,并进行回调
     */
    public void forceUpdate(RsCheckRequestCallback callback) {
        RsOtaAgent.getInstance().forceUpdate(mContext);
        RsOtaAgent.getInstance().setCheckListener(callback);
    }

    /**
     * 取消检测更新
     */
    public void cancelUpdate() {
        RsOtaAgent.getInstance().cancelUpdate(mContext);
    }

    /**
     * 设置自动检测更新
     *
     * @param callback
     */
    public void autoUpdate(RsCheckRequestCallback callback) {
        RsOtaAgent.getInstance().autoUpdate(mContext);
        RsOtaAgent.getInstance().setCheckListener(callback);
    }

    /**
     * 取消自动检测更新
     */
    public void cancelAutoUpdate() {
        RsOtaAgent.getInstance().cancelAutoUpdate(mContext);
    }


    /**
     * 开始下载
     *
     * @param aPackage
     * @param callback
     */
    public void startDownload(RsFwUpdatePackage aPackage, RsDownloadRequestCallback callback) {
        RsOtaAgent.getInstance().download(mContext, aPackage);
        RsOtaAgent.getInstance().setDownloadRequestCallback(callback);
    }

    /**
     * 取消下载
     */
    public void cancelDownload() {
        RsOtaAgent.getInstance().cancelDownload(mContext);
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() {
        RsOtaAgent.getInstance().pauseDownload(mContext);
    }

    /**
     * 恢复下载
     */
    public void resumeDownload() {
        RsOtaAgent.getInstance().resumeDownload(mContext);
    }


    private class DeviceInfoCallback implements IDeviceInfoCallback {

        /**
         * 获取终端厂商或品牌信息
         *
         * @return
         */
        @Override
        public String getManufacturer() {
            LogManager.getInstance().printLog("FOTA:getManufacturer:Cybic");
            return "Cybic";
        }

        /**
         * 获取设备型号
         *
         * @return
         */
        @Override
        public String getModel() {
            LogManager.getInstance().printLog("FOTA:getModel:1000I");
            return "1000I";
        }

        @Override
        public String getDeviceId() {
            String deviceId = mTelephonyManager.getDeviceId();
            LogManager.getInstance().printLog("FOTA:getDeviceId:" + deviceId);
            return deviceId;
        }

        @Override
        public String getCarrier() {
           /* String subscriberId = mTelephonyManager.getSubscriberId();
            LogManager.getInstance().printLog("FOTA:getCarrier:" + subscriberId);
            return subscriberId;*/
            return null;
        }

        @Override
        public String getFirmwareVersion() {
            String display = Build.DISPLAY;
            LogManager.getInstance().printLog("FOTA:getFirmwareVersion:" + display);
            return display;
        }

        @Override
        public String getSystemReleaseVersion() {
            String systemReleaseVersion = Build.VERSION.SDK_INT + "";
            LogManager.getInstance().printLog("FOTA:getSystemReleaseVersion:" + systemReleaseVersion);
            return systemReleaseVersion;
        }

        @Override
        public String getBuildTime() {
            return null;
        }
    }

    private class ConfigCallback implements IConfigCallback {

        @Override
        public int getCheckInterval() {
            return 0;
        }

        @Override
        public String getOTAServerUrl() {
            return null;
        }

        @Override
        public String getOTAReportServerUrl() {
            return null;
        }

        @Override
        public boolean isAutoDownloadUPOnlyWifi() {
            return false;
        }

        @Override
        public String getUPDownloadPath() {
            LogManager.getInstance().printLog("下载路径:" + Environment.getDataDirectory() + "/data/com.vogtec.ibx5/files");
            return Environment.getDataDirectory() + "/data/com.vogtec.ibx5/files";

        }

        @Override
        public boolean isUPDownloadPathStorageAvailable() {
            long sdFreeSize = FileUtils.getSDFreeSize();//MB
            LogManager.getInstance().printLog("固件升级包存储空间是否满足:" + sdFreeSize);
            if (sdFreeSize > 300) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean isDevMemoryAvailable() {

            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            mMyActivityManager.getMemoryInfo(memoryInfo);
            long availMemSize = memoryInfo.availMem;

            return true;
        }

        @Override
        public boolean isDevBatteryAvailable() {
            //int currentLevel = MainActivity.mInstance.mCurrentLevel;
            /*LogFile.printMQTTConnected("当前电量:" + currentLevel);
            if (currentLevel == -1) {
                return true;
            } else if (currentLevel <= 30) {
                return false;
            } else {
                return true;
            }*/
            return true;
        }
    }

    public static String handleErrorCode(int errorCode, boolean isUpdate) {
        int message = -1;
        if (isUpdate) {
            message = R.string.check_update_failure;
        } else {
            message = R.string.download_failure;
        }
        switch (errorCode) {
            case 0:
                //操作成功
                break;
            case -1:
                //操作失败
                break;
            case 10:
                //未知原因
                break;
            case 11:
                //打开文件失败
                break;
            case 12:
                //创建文件失败
                break;
            case 13:
                //无效文件
                break;
            case 14:
                //无效参数
                break;
            case 15:
                //无效URL
                break;
            case 16:
                //终端内存不足
                message = R.string.download_failure_out_of_memory;
                break;
            case 17:
                //存储空间不足
                message = R.string.download_failure_space_enough;
                break;
            case 18:
                //电量不足
                message = R.string.download_failure_low_power;
                break;
            case 19:
                //用户取消
                break;
            case 20:
                //已经被root
                break;
            case 25:
                //本地错误最大值范围
                break;
            case 1600:
                //客户端安装成功
                break;
            case 1601:
                //客户端下载成功
                break;
            case 1602:
                //客户端操作失败
                break;
            case 1603:
                //用户取消
                break;
            case 1604:
                //下载失败
                break;
            case 1605:
                //数据包和机器不匹配
                break;
            case 1606:
                //升级包校验错误
                break;
            case 1607:
                //没有接受安装请求
                break;
            case 1608:
                //下载鉴权失败
                break;
            case 1609:
                //下载请求超时
                break;
            case 1610:
                //下载服务器无法访问
                break;
            case 1500:
                //服务器响应成功
                break;
            case 1501:
                //设备信息和服务器端不匹配
                break;
            case 1502:
                //服务器不支持
                break;
            case 200:
                //请求成功
                break;
            case 400:
                //请求参数错误
                break;
            case 404:
                //请求失败
                break;
            case 500:
                //服务发生错误
                break;
            case 900:
                //读取数据超时
                break;
            case 901:
                //连接超时
                break;
            case 902:
                //网络中断
                break;
            case 903:
                //网络数据流传输出错
                break;
            case 904:
                //服务器忙
                break;
            case 905:
                //缺省错误
                break;
        }
        if (message != -1) {
            return ResourceUtils.getString(message);
        }
        return "";
    }
}
