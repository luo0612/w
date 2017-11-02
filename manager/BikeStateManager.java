package com.vogtec.ibx5.manager;

import com.vogtec.core.BikeStatus;
import com.vogtec.core.BikeStatusListener;
import com.vogtec.core.BikeStatusManager;
import com.vogtec.ibx5.base.Constants;
import com.vogtec.utils.Utils;

/**
 * Created by PC on 2017/3/7.
 */

public class BikeStateManager {
    private static BikeStateManager manager = new BikeStateManager();
    private final BikeStatusManager bikeStatusManager;

    private BikeStateManager() {
        bikeStatusManager = (BikeStatusManager) Utils.getContext().getSystemService(Constants.BIKE_STATUS_SERVICE);
        bikeStatusManager.start();
        bikeStatusManager.startListening(bikeStatusListener);
    }

    public static BikeStateManager getInstance() {
        return manager;
    }

    private BikeStatusListener bikeStatusListener = new BikeStatusListener() {
        public void mcuStatus(BikeStatus stat) {
            //lock:1,unlock:0
            int lock = stat.lock;
        }
    };

    public void refresh() {
        bikeStatusManager.mcuStatus();
    }

    public void unregister() {
        bikeStatusManager.stopListening();
    }
}
