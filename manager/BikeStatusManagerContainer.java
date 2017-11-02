package com.vogtec.ibx5.manager;

import com.vogtec.core.BikeStatus;
import com.vogtec.core.BikeStatusListener;
import com.vogtec.core.BikeStatusManager;
import com.vogtec.ibx5.base.Constants;
import com.vogtec.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 2017/3/9.
 */

public class BikeStatusManagerContainer extends BikeStatusListener {
    private static BikeStatusManagerContainer bikeStatusManagerContainer = new BikeStatusManagerContainer();
    private final BikeStatusManager mBikeStatusManager;
    private List<BikeStatusManagerListener> mListeners = new ArrayList<BikeStatusManagerListener>();
    private BikeStatus mBikeStatus;

    private BikeStatusManagerContainer() {
        mBikeStatusManager = (BikeStatusManager) Utils.getContext().getSystemService(Constants.BIKE_STATUS_SERVICE);
        mBikeStatusManager.start();
        mBikeStatusManager.changeReportingPeriod(BikeStatusManager.MIN_REPORT_PERIOD);
        mBikeStatusManager.startListening(this);
    }

    public static BikeStatusManagerContainer getInstance() {
        return bikeStatusManagerContainer;
    }

    public BikeStatusManager getBikeStatusManager() {
        return mBikeStatusManager;
    }

    @Override
    public void mcuStatus(BikeStatus stat) {
        super.mcuStatus(stat);
        for (BikeStatusManagerListener listener : mListeners) {
            listener.onChanged(stat);
        }
    }

    public BikeStatus getBikeStatus() {
        refreshStatus();
        return mBikeStatus;
    }

    public void refreshStatus() {
        mBikeStatusManager.mcuStatus();
    }

    public void register(BikeStatusManagerListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void unregister(BikeStatusManagerListener listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public interface BikeStatusManagerListener {
        void onChanged(BikeStatus status);
    }
}

