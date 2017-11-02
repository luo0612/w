package com.vogtec.ibx5.manager;

/**
 * Created by PC on 2017/3/9.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.vogtec.core.BikeStatus;
import com.vogtec.ibx5.base.Constants;
import com.vogtec.light.BikeLightListener;
import com.vogtec.light.BikeLightManager;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 灯光控制器
 */
public class LightManager extends BikeLightListener implements BikeStatusManagerContainer.BikeStatusManagerListener {
    private static final int LIGHT_EXECUTE_INTERVAL = 150;
    private static LightManager lightManager = new LightManager();
    private BikeLightManager mBikeLightManager;
    private boolean isTail;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private List<LightChangedListener> mListeners = new ArrayList<>();
    private boolean isHead;
    private boolean isHeadOutline;
    private boolean isTailOutline;

    private LightManager() {
        init();
    }

    public static LightManager getInstance() {
        return lightManager;
    }

    private void init() {
        mBikeLightManager = (BikeLightManager) Utils.getContext().getSystemService(Constants.BIKELIGHT_SERVICE);
        BikeStatusManagerContainer.getInstance().register(this);
    }

    @Override
    public void onChanged(BikeStatus status) {
        boolean dayLight = status.dayLight > 0;
        boolean headLight = status.headLight > 0;
        boolean laserLight = status.laserLight > 0;
        boolean tailLight = status.tailLight > 0;

        if (headLight != isHead) {
            isHead = headLight;
            notifyLightChanged(BikeLightManager.HEAD, isHead);
        }

        if (laserLight != isTailOutline) {
            isTailOutline = laserLight;
            notifyLightChanged(BikeLightManager.LASER_LAMP, isTailOutline);
        }

        if (tailLight != isTail) {
            isTail = tailLight;
            notifyLightChanged(BikeLightManager.TAIL, isTail);
        }

    }

    public void setAutoLight() {
        boolean auto = SPUtils.getBoolean(Constants.AUTO_LIGHT, true);
        if (auto) {

        } else {

        }
    }

    public void closeAll() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                boolean isNight = false;
                openTailLight(isNight);
                sleepInterval();
                openHeadLight(isNight);
                sleepInterval();
                openHeadOutlineLight(isNight);
                sleepInterval();
                openTailOutlineLight(isNight);
            }
        };
        thread.start();
    }

    public void openAll() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                boolean isNight = true;
                openTailLight(isNight);
                sleepInterval();
                openHeadLight(isNight);
                sleepInterval();
                openHeadOutlineLight(isNight);
                sleepInterval();
                openTailOutlineLight(isNight);
            }
        };
        thread.start();
    }

    private boolean openTailOutlineLight(boolean isNight) {
        boolean openLight = false;
        openLight = openLight(BikeLightManager.LASER_LAMP, isNight);
        isTailOutline = isNight;
        notifyLightChanged(BikeLightManager.LASER_LAMP, isNight);
        return openLight;
    }

    private boolean openHeadOutlineLight(boolean isNight) {
        boolean openLight = openLight(BikeLightManager.OUTLINE_MARKER_LAMP,
                isNight);
        isHeadOutline = isNight;
        notifyLightChanged(BikeLightManager.OUTLINE_MARKER_LAMP, isNight);
        return openLight;
    }

    private boolean openHeadLight(boolean isNight) {
        boolean openLight = openLight(BikeLightManager.HEAD, isNight);
        sleepInterval();
        openLight(BikeLightManager.SIDE_DECORATE, isNight);
        isHead = isNight;
        notifyLightChanged(BikeLightManager.HEAD, isNight);
        return openLight;
    }

    private void sleepInterval() {
        SystemClock.sleep(LIGHT_EXECUTE_INTERVAL);
    }

    private boolean openTailLight(boolean isNight) {
        boolean openLight = openLight(BikeLightManager.TAIL, isNight);
        isTail = isNight;
        notifyLightChanged(BikeLightManager.TAIL, isNight);
        return openLight;
    }

    private void notifyLightChanged(final int index, final boolean isNight) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (LightChangedListener listener : mListeners) {
                    listener.onLightChanged(index, isNight);
                }
            }
        });
    }

    public boolean openLight(int index, boolean open) {
        int result = 0;
        if (open) {
            result = mBikeLightManager.open(index);
        } else {
            result = mBikeLightManager.close(index);
        }
        return true;
    }

    @Override
    public void onTailOpen(int flg) {
        super.onTailOpen(flg);

    }


    public interface LightChangedListener {
        void onLightChanged(int index, boolean enable);
    }

    public void registerLightChangedListener(LightChangedListener listener) {
        mListeners.add(listener);
    }

    public void unregisterListChangedListener(LightChangedListener listener) {
        mListeners.remove(listener);
    }

    /**
     * 开始监听
     */
    public void startListener() {
        mBikeLightManager.startListening(this);
    }

    /**
     * 停止监听
     */
    public void stopListener() {
        mBikeLightManager.stopListening();
    }
}
