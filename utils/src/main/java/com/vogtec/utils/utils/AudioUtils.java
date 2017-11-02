package com.vogtec.utils.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.vogtec.utils.Utils;
import com.vogtec.utils.domain.Music;

import java.util.ArrayList;

public abstract class AudioUtils {
    private static final Context CONTEXT = Utils.getContext();

    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<Music> getAllMusics() {

        ArrayList<Music> musics = null;

        Cursor cursor = CONTEXT.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,//文件名
                        MediaStore.Audio.Media.TITLE,//歌曲名
                        MediaStore.Audio.Media.DURATION,//时长
                        MediaStore.Audio.Media.ARTIST,//歌手名
                        MediaStore.Audio.Media.ALBUM,//时长
                        MediaStore.Audio.Media.YEAR,//年代
                        MediaStore.Audio.Media.MIME_TYPE,//文件类型
                        MediaStore.Audio.Media.SIZE,//文件大小
                        MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[]{"audio/mpeg", "audio/x-ms-wma"}, null);

        musics = new ArrayList<Music>();

        if (cursor.moveToFirst()) {

            Music music = null;

            do {
                music = new Music();
                // 文件名
                music.setFileName(cursor.getString(1));
                // 歌曲名
                music.setTitle(cursor.getString(2));
                // 时长
                music.setDuration(cursor.getInt(3));
                // 歌手名
                music.setSinger(cursor.getString(4));
                // 专辑名
                music.setAlbum(cursor.getString(5));
                // 年代
                if (cursor.getString(6) != null) {
                    music.setYear(cursor.getString(6));
                } else {
                    music.setYear("未知");
                }
                // 歌曲格式
                if ("audio/mpeg".equals(cursor.getString(7).trim())) {
                    music.setType("mp3");
                } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
                    music.setType("wma");
                }
                // 文件大小
                if (cursor.getString(8) != null) {
                    float size = cursor.getInt(8) / 1024f / 1024f;
                    music.setSize((size + "").substring(0, 4) + "M");
                } else {
                    music.setSize("未知");
                }
                // 文件路径
                if (cursor.getString(9) != null) {
                    music.setFileUrl(cursor.getString(9));
                }
                musics.add(music);
            } while (cursor.moveToNext());

            cursor.close();

        }
        return musics;
    }

}