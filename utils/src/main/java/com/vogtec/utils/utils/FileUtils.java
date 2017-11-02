package com.vogtec.utils.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.vogtec.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/31.
 * 文件工具类
 */
public abstract class FileUtils {
    private static Context mContext = Utils.getContext();
    private static final String TAG = FileUtils.class.getSimpleName();
    /**
     * 文件大小的单位:B
     */
    public static final int SIZETYPE_B = 1;
    /**
     * 文件大小的单位:KB
     */
    public static final int SIZETYPE_KB = 2;
    /**
     * 文件大小的单位:MB
     */
    public static final int SIZETYPE_MB = 3;
    /**
     * 文件大小的单位:GB
     */
    public static final int SIZETYPE_GB = 4;


    /**
     * 指定文件或目录的大小,不带后缀
     *
     * @param filePath 文件或目录的路径
     * @param sizeType 单位的类型
     * @return
     */
    public static double getFileOrDirectorySize(String filePath, int sizeType) {
        File file = new File(filePath);
        //块的大小
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getDirectorySize(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "获取文件或目录的大小失败");
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 根据指定类型,格式化文件大小
     *
     * @param blockSize
     * @param sizeType
     * @return 文件的大小, 不带后缀
     */
    private static double formatFileSize(long blockSize, int sizeType) {
        //Decimal:小数
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSize = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSize = Double.valueOf(df.format(blockSize));
                break;
            case SIZETYPE_KB:
                fileSize = Double.valueOf(df.format(blockSize / 1024));
                break;
            case SIZETYPE_MB:
                fileSize = Double.valueOf(df.format(blockSize / 1048576));
                break;
            case SIZETYPE_GB:
                fileSize = Double.valueOf(df.format(blockSize / 1073741824));
                break;
        }
        return fileSize;
    }

    /**
     * 获取文件的大小
     *
     * @param file
     * @return
     */
    private static long getFileSize(File file) throws IOException {
        long size = 0;
        if (file.exists()) {
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(file);
            //acailable:有效的,可用的
            size = fileInputStream.available();
            IOUtils.close(fileInputStream, null);
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取目录的大小
     *
     * @param file
     * @return
     */
    private static long getDirectorySize(File file) throws IOException {
        long size = 0;
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) {
                size += getDirectorySize(f);
            } else {
                size += getFileSize(f);
            }
        }
        return size;
    }


    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static String getSDCardRootDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取SD卡下的应用包名目录
     *
     * @return
     */
    public static String getSDCardAppDir() {
        File file = new File(getSDCardRootDir() + "/" + mContext.getPackageName());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取SD卡下的下载缓存目录
     *
     * @return
     */
    public static String getSDCardDownloadCacheDir() {
        return Environment.getDownloadCacheDirectory().getAbsolutePath();
    }


    /**
     * 获取SD卡下的用户数据目录
     *
     * @return
     */
    public static String getSDCardDataDir() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * 根据指定路径删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除指定文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取文件目录
     *
     * @param filePath
     * @return
     */
    public static File getDirectory(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取图片的路径,通过系统相册
     *
     * @param data
     * @return
     */
    public static String getFilePathForSystemPhoto(Intent data) {
        if (data == null) {
            return null;
        }

        Cursor cursor = null;
        try {
            // 从系统相册获取图片
            Uri uri = data.getData();// 获取图片的uri地址
            // 通过内容提供者获取游标
            cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String photoPath = cursor.getString(1);
            return photoPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    /**
     * 获取文件的磁盘路径:path
     *
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(mContext, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(mContext, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(mContext, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(mContext, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 获取指定路径下,指定的后缀的文件
     *
     * @param filePath
     * @param suffix
     * @return
     */
    public static List<String> getAllFilePath(String filePath, String suffix) {
        //保存所有的指定的文件
        List<String> filePaths = new ArrayList<String>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("指定目录不存在");
        }
        if (file.isFile()) {
            //说明指定的路径是文件
            //判断文件是否以指定的后缀结尾
            String path = file.getAbsolutePath();
            if (path.endsWith(suffix)) {
                //说明是以指定的后缀结尾
                filePaths.add(path);
            }

        } else {
            filePaths = getAllFile(file, suffix, filePaths);
        }
        return filePaths;

    }

    private static List<String> getAllFile(File file, String suffix, List<String> filePaths) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFile(f, suffix, filePaths);
                } else if (f.exists() && f.canRead() && f.getAbsolutePath().endsWith(suffix)) {
                    filePaths.add(f.getAbsolutePath());
                }
            }
        }
        return filePaths;
    }

    /**
     * 获取内置或外置SdCard
     *
     * @param is_removale true:外置,false:内置
     * @return
     */
    public static String getStoragePath(boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取SD卡剩余的空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }


}
