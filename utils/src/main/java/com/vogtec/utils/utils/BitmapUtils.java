package com.vogtec.utils.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by admin on 2016/8/31.
 * Bitmap图片工具类
 */
public abstract class BitmapUtils {
    /**
     * 对图片进行Base64编码
     *
     * @return
     */
    public static String compressByBase64(String filePath) {
        Bitmap bitmap = null;
        bitmap = getCompressBitmap(filePath, 720, 1280);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        String bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return bitmapString;

    }

    /**
     * 根据图片路径,获取到压缩后的图片
     *
     * @param filePath
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap getCompressBitmap(String filePath, int targetWidth, int targetHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            options.inSampleSize = getSampleSize(options, targetWidth, targetHeight);
            options.inJustDecodeBounds = false;
            Bitmap compressBitmap = BitmapFactory.decodeFile(filePath, options);
            //读取文件的旋转角度
            int degree = readBitmapDegree(filePath);
            if (degree != 0) {
                //对图片对象进行旋转
                compressBitmap = rotateBitmap(compressBitmap, degree);
            }
            return compressBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 生成图片文件
     *
     * @param filePath
     * @param bitmap
     */
    public static boolean createImageFile(String filePath, Bitmap bitmap) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            boolean create = bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            return create;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成图片文件
     *
     * @param output
     * @param bitmap
     * @return
     */
    public static boolean crateImageFile(OutputStream output, Bitmap bitmap) {
        boolean create = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        return create;
    }

    /**
     * 旋转Bitmap对象
     *
     * @param compressBitmap
     * @param degree
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap compressBitmap, int degree) {
        if (compressBitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            compressBitmap = Bitmap.createBitmap(compressBitmap, 0, 0, compressBitmap.getWidth(), compressBitmap.getHeight(), matrix, true);
        }
        return compressBitmap;
    }

    /**
     * 读取文件的旋转角度
     *
     * @param filePath
     * @return
     */
    private static int readBitmapDegree(String filePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            //orientation:方向
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 获取图片的取样比例
     *
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    private static int getSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > targetHeight || width > targetWidth) {
            int widthRatio = Math.round((float) width / (float) targetWidth);
            int heightRatio = Math.round((float) height / (float) targetHeight);
            if (widthRatio > heightRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        return inSampleSize;
    }

    /**
     * 生成二维码
     *
     * @param path
     * @return
     */
    public static Bitmap createQRCode(String path) {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(path, BarcodeFormat.QR_CODE, 500, 500);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 获取视频的缩略图
     *
     * @param videoPath
     * @return
     */
    public Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }

}
