package com.vogtec.ibx5.manager;

import android.content.Context;
import android.content.Intent;

import com.vogtec.ibx5.services.MusicService;

/**
 * Created by PC on 2017/3/6.
 */

/**
 * 控制音乐播放
 */
public class MusicManager {
    public static final String ACTION_RING_PLAY = "com.vogtec.action.RING_PLAY";
    public static final String ACTION_RING_STOP = "com.vogtec.action.RING_STOP";
    public static final String ACTION_MUSIC_REFRESH = "com.vogtec.action.MUSIC_REFRESH";

    private Context mContext;

    private static MusicManager musicManager = null;

    private MusicManager(Context context) {
        mContext = context;
    }

    public synchronized static MusicManager getInstance(Context context) {
        if (musicManager == null) {
            musicManager = new MusicManager(context);
        }
        return musicManager;
    }


    public void stop() {
        sendBroadcast(ACTION_RING_STOP);
    }

    public void start() {
        sendBroadcast(ACTION_RING_PLAY);
    }

    public void previous() {
        sendBroadcast(MusicService.ACTION_PREVIOUS);
    }

    public void next() {
        sendBroadcast(MusicService.ACTION_NEXT);
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mContext.sendBroadcast(intent);
    }

    public void refresh() {
        sendBroadcast(ACTION_MUSIC_REFRESH);
    }
}
