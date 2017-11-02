package com.vogtec.utils.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2016/8/31.
 * MD5工具类
 */
public abstract class MD5Utils {
    /**
     * 对字符串进行MD5加密
     *
     * @param text
     * @return
     */
    public static String stringToMD5(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(text.getBytes());
            byte[] digest = messageDigest.digest();
            return byteArrayToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String byteArrayToHex(byte[] digest) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[digest.length * 2];
        int index = 0;
        for (byte b : digest) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xF];
            resultCharArray[index++] = hexDigits[b & 0xF];
        }
        return new String(resultCharArray);
    }
}
