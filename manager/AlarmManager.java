package com.vogtec.ibx5.manager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.vogtec.ibx5.R;
import com.vogtec.ibx5.services.MusicService;
import com.vogtec.utils.Utils;
import com.vogtec.utils.utils.LogUtils;

import java.io.FileDescriptor;

/**
 * Created by PC on 2017/3/3.
 */

public class AlarmManager {

    private static final AlarmManager MANAGER = new AlarmManager();
    private static AudioManager mAudioManager;
    private static int mMusicMaxVolume;
    private boolean mIsPlaying = false;
    private MediaPlayer mMediaPlayer;
    private int mMusicCurrentVolume;

    public static AlarmManager getInstance() {
        mAudioManager = (AudioManager) Utils.getContext().getSystemService(Context.AUDIO_SERVICE);
        //获取音乐最大声和当前的声音
        mMusicMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return MANAGER;
    }

    public void startSpeaker() {
        if (!mIsPlaying) {
            Utils.getContext().sendBroadcast(new Intent(MusicService.ACTION_ALARM_START_PUASE));
            playRingtone();
            changeVoiceToMax();
            mIsPlaying = true;
        }
    }

    private void changeVoiceToMax() {
        mMusicCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mMusicCurrentVolume != mMusicMaxVolume) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMusicMaxVolume, AudioManager.FLAG_VIBRATE);
        }
    }

    public void stopSpeaker() {
        if (mIsPlaying) {
            changeVoiceToCurrent();
            releaseMediaPlayer();
            mIsPlaying = false;
            Utils.getContext().sendBroadcast(new Intent(MusicService.ACTION_ALARM_STOP_PLAY));
        }
    }

    private void changeVoiceToCurrent() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mMusicCurrentVolume, AudioManager.FLAG_VIBRATE);
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void playRingtone() {
        releaseMediaPlayer();//释放播放资源
        int resId = R.raw.security_1;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor descriptor = Utils.getContext().getResources().openRawResourceFd(resId);
        try {
            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
            LogUtils.e(this, fileDescriptor.toString());
            mMediaPlayer.setDataSource(fileDescriptor, descriptor.getStartOffset(), descriptor.getDeclaredLength());
            descriptor.close();
            mMediaPlayer.prepare();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否正在报警
     *
     * @return
     */
    public boolean isPlaying() {
        return mIsPlaying;
    }
}
