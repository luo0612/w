package com.vogtec.ibx5.mqtt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.vogtec.ibx5.activity.MainActivity;
import com.vogtec.ibx5.base.Api;
import com.vogtec.ibx5.base.BaseActivity;
import com.vogtec.ibx5.base.BikeState;
import com.vogtec.ibx5.base.Constants;
import com.vogtec.ibx5.db.CyclingTaskDBHelper;
import com.vogtec.ibx5.db.LockDBHelper;
import com.vogtec.ibx5.domain.BikeCyclingTask;
import com.vogtec.ibx5.fragment.CyclingFragment;
import com.vogtec.ibx5.manager.BroadcastManager;
import com.vogtec.ibx5.manager.LightManager;
import com.vogtec.ibx5.manager.LockManager;
import com.vogtec.ibx5.manager.LogManager;
import com.vogtec.ibx5.manager.MessageManager;
import com.vogtec.ibx5.manager.ShareBatteryManager;
import com.vogtec.ibx5.manager.SpeakerManager;
import com.vogtec.ibx5.services.RepairLockService;
import com.vogtec.ibx5.utils.LogFile;
import com.vogtec.ibx5.utils.LogManagerUtils;
import com.vogtec.ibx5share.R;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.JsonUtils;
import com.vogtec.utils.utils.LogUtils;
import com.vogtec.utils.utils.SPUtils;
import com.vogtec.utils.utils.ToastUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.vogtec.ibx5.utils.LogManagerUtils.printErrorLog;
import static com.vogtec.ibx5.utils.LogManagerUtils.printLog;

public class MQTTUtil {

    private final static String TAG = MQTTUtil.class.getCanonicalName();

    public static final String REQUEST_CODE = "com.vogtec.ibx5share.action.REQUEST_CODE";
    public static final String ACTION_CONNECT_MQTT = "com.vogtec.ibx5share.action.CONNECT_MQTT";

    public static final int SUCCESS = 0;
    private static MQTTUtil mMQTTUtil;
    private MqttAndroidClient client;
    private MqttConnectOptions mqttConnectOptions;
    private Context context = Utils.getContext();
    public static byte mToken;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private final Handler mHandler;


    public synchronized static MQTTUtil getInstance() {
        if (mMQTTUtil == null) {
            mMQTTUtil = new MQTTUtil();
        }
        return mMQTTUtil;
    }

    private MQTTUtil() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                if (what < 1000) {
                    handleMqttMessage(msg);
                } else {
                    if (what == Constants.MQTT_CLOSE_LIGHT) {
                        LightManager.getInstance().closeAll();
                    } else if (what == Constants.MQTT_STOP_SPEAKER) {
                        SpeakerManager.getInstance().stopSpeaker();
                    }
                }
            }
        };
    }

    /**
     * 处理Mqtt的下发的消息
     *
     * @param msg
     */
    private void handleMqttMessage(Message msg) {

        RepairLockService.startRepairLockService();

        int type = msg.what;
        byte[] payload = (byte[]) msg.obj;
        if (type == 1) {
            // 获取的Token
            mToken = payload[payload.length - 1];
            LogUtils.e(this, "Token:" + mToken);
            //保存TOKEN
            SPUtils.setInt(Constants.BIKE_TOKEN, mToken);
            LogFile.printMQTTConnected("接受到MQTT服务端的开锁指令:  " + mToken);
            LogFile.printToken(mToken + "");
            //进行确认开锁上传
            publishConfirmUnlock();
        } else if (type == 2) {
            //UTC时间
//            类型（TYPE）	1	值为2，非ASCII
//            年（Year）	4	示例：2017
//            月（Month）	2	示例：03
//            日（Day）	2	示例：02
//            时（Hour）	2	示例：08
//            分（Minute）	2	示例：32
//            秒（second）	2	示例：38
            byte[] yearBytes = new byte[4];
            System.arraycopy(payload, 1, yearBytes, 0, 4);
            //String year = builder.append().append(payload[2]).append(payload[3]).append(payload[4]).toString();
            String year = new String(yearBytes);


            byte[] monthBytes = new byte[2];
            System.arraycopy(payload, 5, monthBytes, 0, 2);
            String month = new String(monthBytes);

            byte[] dayBytes = new byte[2];
            System.arraycopy(payload, 7, dayBytes, 0, 2);
            String day = new String(dayBytes);

            byte[] hourBytes = new byte[2];
            System.arraycopy(payload, 9, hourBytes, 0, 2);
            String hour = new String(hourBytes);


            byte[] minuteBytes = new byte[2];
            System.arraycopy(payload, 11, minuteBytes, 0, 2);
            String minute = new String(minuteBytes);

            byte[] secondBytes = new byte[2];
            System.arraycopy(payload, 13, secondBytes, 0, 2);
            String second = new String(secondBytes);

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                String time = year + month + day + hour + minute + second;
                Date date = dateFormat.parse(time);
                LogManagerUtils.printLog("服务端的时间:time:" + time + "====date:" + date);
                long dateTime = date.getTime();
                Utils.getContext().enforceCallingOrSelfPermission("android.permission.SET_TIME", "setDate");
                //TODO 设置GMT+0时区的
                SystemClock.setCurrentTimeMillis(dateTime);//修改系统的当前时间
                //RepairLockService.startRepairLockService();
                if (MainActivity.mInstance != null) {
                    MainActivity.mInstance.getSettingFragment().init();
                    MainActivity.mInstance.refreshTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogManagerUtils.printErrorLog(e);
            }
        } else if (type == 3) {
            byte time = payload[payload.length - 1];
            //闪灯指令
            LightManager.getInstance().openAll();
            mHandler.sendEmptyMessageDelayed(Constants.MQTT_CLOSE_LIGHT, time * 1000);
        } else if (type == 4) {
            byte time = payload[payload.length - 1];
            //响铃指令
            SpeakerManager.getInstance().startSpeaker();
            mHandler.sendEmptyMessageDelayed(Constants.MQTT_STOP_SPEAKER, time * 1000);
        } else if (type == 5) {
            //状态重置
            BikeCyclingTask task = BikeState.getInstance().getCurrentBikeCyclingTask();
            if (task != null) {
                //说明有骑行任务
                boolean pauseLock = task.isPauseLock();
                if (pauseLock) {
                    //说明是暂停状态
                    BroadcastManager
                            .newInstance("lesports.intent.action.BIKE_SERVICE_METHOD_RESPONSE")
                            .putExtra("method", "lock")
                            .putExtra("state", "pause")
                            .send();

                } else {
                    //说明不是暂停状态
                    LockManager.getInstance().lock();
                    BroadcastManager
                            .newInstance("lesports.intent.action.BIKE_SERVICE_METHOD_RESPONSE")
                            .putExtra("method", "lock")
                            .putExtra("state", "power")
                            .send();
                }

            } else {
                //说明没有骑行任务
            }

        } else if (type == 9) {
            //重新获取二维码
            BroadcastManager
                    .newInstance(MQTTUtil.REQUEST_CODE)
                    .send();

        } else if (type == 7) {
            printLog("获取mqtt服务器的地址:" + Arrays.toString(payload));
            printLog("获取mqtt服务器的地址:====>" + new String(payload));
            //mqtt服务器的地址
                /*
                类型（TYPE）	1	值为7，非ASCII  startIndex:0
                车牌号（bikeNum）	8	ASCII  startIndex:1
                MQTT端口长度（port len）	1	非ASCII，int8 8
                MQTT端口（port）	port len	ASCII 8+portLen
                MQTT ip地址长度（ip len）	1	非ASCII 8+portLen+1
                MQTT ip地址（ip addr）	ip len	ASCII
                 */
            byte[] bikeNumberBytes = new byte[8];//自行车号
            System.arraycopy(payload, 1, bikeNumberBytes, 0, 8);
            printLog("服务器端的车牌号:" + new String(bikeNumberBytes));
            int portLen = payload[9];
            printLog("端口的长度:" + portLen);
            byte[] portBytes = new byte[portLen];//端口号
            System.arraycopy(payload, 10, portBytes, 0, portLen);
            printLog("端口:" + new String(portBytes));
            int ipLen = payload[10 + portLen];
            printLog("IP的长度:" + ipLen);
            ToastUtils.showToastShortForLog("IP的长度:" + ipLen);
            byte[] ipBytes = new byte[ipLen];//ip地址
            System.arraycopy(payload, (11 + portLen), ipBytes, 0, ipLen);
            printLog("ip地址:" + new String(ipBytes));

                /*
                118.190.0.209------->Fri May 26 16:26:43 GMT+08:00 2017
                服务器端的车牌号:12345678------->Fri May 26 16:26:43 GMT+08:00 2017
                端口的长度:3------->Fri May 26 16:26:43 GMT+08:00 2017
                端口:123------->Fri May 26 16:26:43 GMT+08:00 2017
                IP的长度:13------->Fri May 26 16:26:43 GMT+08:00 2017
                ip地址:118.190.0.209------->Fri May 26 16:26:43 GMT+08:00 2017
                 */
            String mqttIp = "tcp://" + new String(ipBytes) + ":" + new String(portBytes);
            printLog("mqttIp的服务端地址:" + mqttIp);
            SPUtils.setString(Constants.MQTT_IP, mqttIp);
            MqttConstants.MQTT_HOST = mqttIp;

        } else if (type == 10) {
            //http网关地址
                /*
                类型（TYPE）	1	值为10，非ASCII
                车牌号（bikeNum）	8	ASCII
                HTTP端口长度（port len）	1	非ASCII，int8
                HTTP端口（port）	port len	ASCII
                HTTP ip地址长度（ip len）	1	非ASCII
                HTTP ip地址（ip addr）	ip len	ASCII
                 */
            printLog("获取http服务器的地址:" + Arrays.toString(payload));
            printLog("获取http服务器的地址:====>" + new String(payload));
            byte[] bikeNumberBytes = new byte[8];//自行车号
            System.arraycopy(payload, 1, bikeNumberBytes, 0, 8);
            printLog("http的i自行车车牌号:" + new String(bikeNumberBytes));
            int portLen = payload[9];
            printLog("http的i端口长度:" + portLen);
            byte[] portBytes = new byte[portLen];//端口号
            System.arraycopy(payload, 10, portBytes, 0, portLen);
            printLog("http的i端口号:" + new String(portBytes));
            int ipLen = payload[10 + portLen];
            printLog("http的iip长度:" + ipLen);
            byte[] ipBytes = new byte[ipLen];//ip地址
            System.arraycopy(payload, (11 + portLen), ipBytes, 0, ipLen);
            printLog("http的ip地址:" + new String(ipBytes));

            String httpIp = "http://" + new String(ipBytes) + ":" + new String(portBytes);
            printLog("htttIp的服务端地址:" + httpIp);

            SPUtils.setString(Constants.HTTP_IP, httpIp);
            Api.HOST = httpIp;

        } else if (type == 12) {
            LogManagerUtils.printLog("上报电量的指令.....type=" + 12);
            ShareBatteryManager.getInstance().publishBattery();
        } else if (type == 14) {
            BikeCyclingTask task = BikeState.getInstance().getCurrentBikeCyclingTask();

            byte token = payload[payload.length - 1];
            if (task != null) {
               /* int bikeToken = BikeState.getInstance().getCurrentBikeCyclingTask().getBikeToken();
                if (bikeToken == -10000) {
                    bikeToken = SPUtils.getInt(Constants.BIKE_TOKEN, -10000);
                }
                if (bikeToken == token) {
                    boolean pauseLock = task.isPauseLock();
                    if (pauseLock) {
                        LockManager.getInstance().unlock();
                    } else {
                        //LockManager.getInstance().unlock();
                    }
                }*/

                //if (task.isPauseLock()) {
                LockManager.getInstance().unlock();
                //}

            }
        } else if (type == 15) {
            //当进行微信公众号退款时 , 发送该指令 , 头机端获取到该指令进行请求BikeInfo , 判断自行车的状态是否为 1
            //当自行车的状态为 1 时 , 如果自行车没锁车 , 且自行车位于暂停状态 , 会进行自动锁车
            ToastUtils.showToastShortForLog("获取到微信公众号结算的指令type=" + type);
            BroadcastManager.newInstance(BaseActivity.ACTION_REQUEST_BIKE_INFO).send();
        }

    }

    public boolean getIsConnect() {
        if (client != null && client.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 连接MQTT服务器
     */
    public void connect(String mqttHost, String clientId, String userName, String passWord) {
        // 服务器地址（协议+地址+端口号）
        if (client == null) {
            client = new MqttAndroidClient(context, mqttHost, clientId);
            // 设置MQTT监听并且接受消息
            client.setCallback(mqttCallback);
            mqttConnectOptions = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            mqttConnectOptions.setCleanSession(true);//设置为true
            // 设置超时时间，单位：秒
            mqttConnectOptions.setConnectionTimeout(5 * 60);
            // 设置会话心跳时间，单位：秒，服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            mqttConnectOptions.setKeepAliveInterval(5 * 60);
            // 设置自动重连
            mqttConnectOptions.setAutomaticReconnect(true);
            // 用户名
            mqttConnectOptions.setUserName(userName);
            // 密码
            mqttConnectOptions.setPassword(passWord.toCharArray());
        }
        doClientConnection();
    }

    /**
     * 断开MQTT连接
     */
    public void disconnect() {
        LogUtils.d(TAG, "disconnect...");
        try {
            if (client != null) {
                client.disconnect();
                client = null;
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布消息
     *
     * @param topic 消息主题
     * @param msg   消息内容
     */
    public void publish(final String topic, byte[] msg) {
        Integer qos = 1;
        Boolean retained = false;
        if (client != null && client.isConnected()) {
            try {
                client.publish(topic, msg, qos.intValue(), retained.booleanValue());
            } catch (MqttException e) {
                e.printStackTrace();
                LogFile.printLog(e);
            }
        } else {
            LogUtils.e(TAG, "publish() connection break, reconnection...");
        }
    }

    public void publish(final String topic, byte[] msg, final OnPublishCallback callback) {
        Integer qos = 1;
        Boolean retained = false;
        if (client != null && client.isConnected()) {
            try {
                client.publish(topic, msg, qos.intValue(), retained.booleanValue(), null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken iMqttToken) {
                        //说明发布消息成功
                        if (callback != null) {
                            callback.success();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        //说明发布消息失败
                        if (callback != null) {
                            callback.failure();
                        }
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    // 在子线程中进行连接
    private void doClientConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!client.isConnected() && isConnectIsNormal()) {
                    try {
                        client.connect(mqttConnectOptions, null,
                                new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken token) {
                                        //设置网络无法访问弹出的次数为0
                                        SPUtils.setInt(Constants.NETWORK_CANNOT_ACCESS, 0);
                                        //设置自行车的状态,Mqtt连接成功
                                        BikeState.getInstance().setMqttConnectedState(true);
                                        LogManagerUtils.printMqttLog(TAG, "MQTT连接服务器成功");
                                       /* if (mAlarmManager != null && mPendingIntent != null) {
                                            mAlarmManager.cancel(mPendingIntent);
                                            mAlarmManager = null;
                                            mPendingIntent = null;
                                        }*/
                                        //发送连接服务器成功的消息
                                        Message message = new Message();
                                        message.what = SUCCESS;
                                        MessageManager.getInstance().getHandler().sendMessage(message);
                                    }

                                    @Override
                                    public void onFailure(IMqttToken arg0, Throwable arg1) {
                                        //设置自行车的状态,Mqtt的连接状态连接失败
                                        BikeState.getInstance().setMqttConnectedState(false);
                                        //TODO 注销进行重连
                                        //reconnect(15 * 1000);
                                        //Log日志
                                        LogManagerUtils.printMqttLog(TAG, "MQTT连接失败");
                                        LogManager.getInstance().printErrorLog(arg1);
                                    }
                                });
                    } catch (MqttException e) {
                        e.printStackTrace();
                        LogFile.printLog(e);
                    }
                }
            }
        }).start();

    }


    /**
     * MQTT监听并且接收消息
     */
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void messageArrived(String topic, MqttMessage message)
                throws Exception {
            // subscribe后得到的消息会执行到这里面
            byte[] payload = message.getPayload();
            byte type = payload[0];
            ToastUtils.showToastShortForLog("type:" + type);
            printLog("type:" + type);

            BikeState.getInstance().setMqttConnectedState(true);//说明mqtt是连接成功的
            Message msg = Message.obtain();
            msg.what = type;
            msg.obj = payload;
            mHandler.sendMessage(msg);

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }

        @Override
        public void connectionLost(Throwable arg0) {
            /*try {
                if (getIsConnect()) {//判断是否和服务器进行连接着的
                    client.disconnect();//先和服务器进行断开,然后再进行重连
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }*/
            //设置自行车的状态,Mqtt的连接状态为false
            BikeState.getInstance().setMqttConnectedState(false);
            LogManagerUtils.printMqttLog(TAG, "MQTT连接已断开");
            //TODO 临时注销进行重连
            //reconnect(20 * 1000);
        }
    };

    private void reconnect(int intervalMillis) {
        //进行重连
        Intent intent = new Intent();
        intent.setAction(ACTION_CONNECT_MQTT);
        mPendingIntent = PendingIntent.getBroadcast(Utils.getContext(), 0, intent, 0);
        mAlarmManager = (AlarmManager) Utils.getContext().getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), intervalMillis, mPendingIntent);
        } else {
            mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + intervalMillis, mPendingIntent);
        }
    }


    /**
     * 连接服务器
     */
    public void connectToServer(String clientId, String userName, String passWord) {
        if (MQTTUtil.isConnectIsNormal()) {
            //设置自行车的网络状态,网络可用
            if (!BikeState.getInstance().isNetworkConnectedState()) {
                BikeState.getInstance().setNetworkConnectedState(true);
            }
            LogUtils.e(this, "HOST:" + MqttConstants.MQTT_HOST + "clientId:" + clientId + ",username:" + userName + ",password:" + passWord);
            LogManagerUtils.printMqttLog(TAG, "HOST:" + MqttConstants.MQTT_HOST + "clientId:" + clientId + ",username:" + userName + ",password:" + passWord);
            connect(MqttConstants.MQTT_HOST, clientId, userName, passWord);
        } else {
            //设置自行车的状态,网络不可用
            if (BikeState.getInstance().isNetworkConnectedState()) {
                BikeState.getInstance().setNetworkConnectedState(false);
            }
            int networkCannotAccess = SPUtils.getInt(Constants.NETWORK_CANNOT_ACCESS, 0);
            if (networkCannotAccess < 3) {
                SPUtils.setInt(Constants.NETWORK_CANNOT_ACCESS, networkCannotAccess + 1);
                Toast.makeText(context, R.string.network_not_access, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 判断网络是否连接
     *
     * @return true 网络连接存在， false 网络连接不存在
     */
    public static boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Utils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            LogUtils.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            LogUtils.i(TAG, "没有可用网络");
            return false;
        }
    }


    public interface OnPublishCallback {
        void success();

        void failure();

    }

    /**
     * 发布确认开锁的信息
     */
    public void publishConfirmUnlock() {
        String bikeNumber = BikeState.getInstance().getBikeNumber();
        if (TextUtils.isEmpty(bikeNumber)) {//如果车牌号为空,则不进行上报
            return;
        }
        int bikeToken = getToken();
        if (bikeToken == -10000) {//如果Token为-10000,则不进行上报
            return;
        }
        byte[] bikeNumberBytes = MsgUtil.getBytesForAscii(bikeNumber);
        byte[] bytes = MsgUtil.addBytes(bikeNumberBytes, new byte[]{(byte) bikeToken, 1});

        publish(LockConstants.CONFIRM_UNLOCK, bytes, new OnPublishCallback() {
            @Override
            public void success() {
                try {
                    int lock = LockManager.getInstance().unlock();
                    if (lock == 0) {
                        //当前没有骑行任务
                        LogManagerUtils.printMqttLog(TAG, "确认开锁的指令上传成功");
                        ToastUtils.showToastShortForLog("开锁成功");
                    } else {
                        //ToastUtils.showToastShort(R.string.unlock_failure);//开锁失败
                    }
                } catch (Exception e) {
                    LogManagerUtils.printMqttLog(TAG, "开锁失败");
                    ToastUtils.showToastShort(R.string.unlock_failure);//开锁失败
                    e.printStackTrace();
                    LogManagerUtils.printErrorLog(e);
                }
            }

            @Override
            public void failure() {
                try {
                    LogManagerUtils.printMqttLog("确认开锁的指令上传失败");
                    ToastUtils.showToastShort(R.string.unlock_failure);//开锁失败
                } catch (Exception e) {
                    e.printStackTrace();
                    printErrorLog(e);
                }
            }
        });
    }

    /**
     * 锁车算账
     */
    public void publishLockAccount() {
        String bikeNumber = BikeState.getInstance().getBikeNumber();
        if (TextUtils.isEmpty(bikeNumber)) {//如果车牌号为空,则不进行上报
            return;
        }
        byte[] bikeNumberBytes = MsgUtil.getBytesForAscii(bikeNumber);
        int bikeToken = getToken();
        if (bikeToken == -10000) {//如果Token为-10000,则不进行上报
            return;
        }
        byte[] bytes = MsgUtil.addBytes(bikeNumberBytes, new byte[]{(byte) bikeToken});
        //说明是正常锁车
        MQTTUtil.getInstance().publish(LockConstants.PUBLISH_LOCK, bytes, new OnPublishCallback() {
            @Override
            public void success() {
                try {
                    LogFile.printMQTTConnected("扫码解锁,结算成功....");
                    stopCycling();
                    LockManager.getInstance().lockForBroadcast();//跳转到锁屏界面
                } catch (Exception e) {
                    e.printStackTrace();
                    LogManagerUtils.printErrorLog(e);
                }
            }

            @Override
            public void failure() {
                try {
                    //锁车结算失败
                    MQTTUtil.getInstance().saveTokenState();//保存Token的状态
                    BikeCyclingTask task = BikeState.getInstance().getCurrentBikeCyclingTask();
                    task.setAccountSuccess(false);
                    //设置Token
                    BikeState.getInstance().getCurrentBikeCyclingTask().setBikeToken(SPUtils.getInt(Constants.BIKE_TOKEN, -10000));
                    //保存当前骑行任务数据到数据库中
                    CyclingTaskDBHelper.getInstance().insertData(JsonUtils.objectToJson(task), new CyclingTaskDBHelper.Callback() {
                        @Override
                        public void onSuccess() {
                            RepairLockService.startRepairLockService();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    //SPUtils.setString(Constants.ACCOUNT_FAILURE, JsonUtils.objectToJson(task));//保存到SP中
                    LogFile.printMQTTConnected("扫码解锁,结算失败,保存数据到数据库中....");
                    stopCycling();//停止骑行将任务置为空
                    LockManager.getInstance().lockForBroadcast();//跳转到锁屏界面
                } catch (Exception e) {
                    LogManagerUtils.printErrorLog(e);
                }
            }
        });//上报锁车信息
    }

    /**
     * 结束骑行,并将当前的骑行任务置为null
     */
    private void stopCycling() {
        if (CyclingFragment.mInstance != null) {
            CyclingFragment.mInstance.stopCycling();
        }
    }

    /**
     * 保存Token的状态
     */
    public void saveTokenState() {
        String bikeNumber = BikeState.getInstance().getBikeNumber();
        if (TextUtils.isEmpty(bikeNumber)) {//如果车牌号为空,则不进行保存
            return;
        }
        int bikeToken = getToken();
        if (bikeToken == -10000) {//如果Token为-10000,则不进行保存
            return;
        }
        LockDBHelper.getInstance().insertToken(bikeNumber, bikeToken);
        LogFile.printMQTTConnected("锁车但是连接已经断开:" + bikeNumber + ",token:" + bikeToken);
        SPUtils.setInt(Constants.DISCONNECTED_BIKE_TOKEN, bikeToken);
    }

    /**
     * 锁车补传
     */
    public void lockAdd(long time, int token, OnPublishCallback callback) {
        String bikeNumber = BikeState.getInstance().getBikeNumber();
        if (TextUtils.isEmpty(bikeNumber)) {//如果车牌号为空,则不进行保存
            return;
        }
        byte[] bikeNumberBytes = MsgUtil.getBytesForAscii(bikeNumber);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        LogFile.printMQTTConnected("锁车补传的时间:" + month + "==" + dayOfMonth + "==" + hourOfDay + "==" + minute);
        int bikeToken = token;
        if (bikeToken == -10000) {//如果Token为-10000,则不进行保存
            return;
        }
        byte[] bytes = MsgUtil.addBytes(bikeNumberBytes, new byte[]{str2Bcd(month + "")[0], str2Bcd(dayOfMonth + "")[0], str2Bcd(hourOfDay + "")[0], str2Bcd(minute + "")[0], (byte) bikeToken});
        MQTTUtil.getInstance().publish(LockConstants.BIKE_LOCK_ADD, bytes, callback);//进行锁车补传
    }

    /**
     * @函数功能: 10进制串转为BCD码
     * @输入参数: 10进制串
     * @输出结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    private int getToken() {
        BikeCyclingTask cyclingTask = BikeState.getInstance().getCurrentBikeCyclingTask();
        int bikeToken = -10000;
        if (cyclingTask == null) {
            bikeToken = SPUtils.getInt(Constants.BIKE_TOKEN, -10000);
            return bikeToken;
        }
        bikeToken = cyclingTask.getBikeToken();
        if (bikeToken == -10000) {
            bikeToken = SPUtils.getInt(Constants.BIKE_TOKEN, -10000);
        }
        if (bikeToken == -10000) {
            bikeToken = LogFile.getToken();
        }
        cyclingTask.setBikeToken(bikeToken);//设置Token到当前骑行任务
        return bikeToken;
    }
}
