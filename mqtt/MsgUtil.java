package com.vogtec.ibx5.mqtt;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MsgUtil {
    private static Random random = new Random();

    /**
     * 根据ASCII码将str 转换为 byte[]
     *
     * @param str 需要转换的字符串
     * @return 转换后的byte[]
     */
    public static byte[] str2bytesByAscii(String str) {
        char[] chars = str.toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    /**
     * 根据ASCII码将 byte[] 转换为str
     *
     * @param bytes 需要转换的byte[]
     * @return 转换后的str
     */
    public static String bytes2strByAscii(byte[] bytes) {
        char[] ch = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ch[i] = (char) bytes[i];
        }
        return String.valueOf(ch);
    }

    public static List<Bike> msg2bike(String msg) {
        int count = 0;
        String amount = msg.substring(1, 5);
        String content = msg.substring(5);
        if (amount.startsWith("0000")) {
            return null;
        } else if (amount.startsWith("000")) {
            count = Integer.valueOf(amount.substring(3));
        } else if (amount.startsWith("00")) {
            count = Integer.valueOf(amount.substring(2));
        } else if (amount.startsWith("0")) {
            count = Integer.valueOf(amount.substring(1));
        } else {
            count = Integer.valueOf(amount);
        }
        List<Bike> bikes = new ArrayList<Bike>();
        int index = 0;
        for (int i = 0; i < count; i++) {
            String single = content.substring(index, index + 28);
            index = index + 28;

           /* LatLng latLng = new LatLng(
                    Double.valueOf(single.substring(10, 20)),
                    Double.valueOf(single.substring(0, 10)));*/

            String bikeNum = single.substring(20);
            String bikeID = bikeNum.substring(2);
            /*Bike bike = new Bike(latLng, bikeID);*/

            double la = Double.valueOf(single.substring(10, 20));
            double lng = Double.valueOf(single.substring(0, 10));
            Bike bike = new Bike(la, lng, bikeID);

            bikes.add(bike);
        }

        return bikes;
    }

    public static UnlockResultMsg analysisUnlockResult(String activityName,
                                                       String msg) {
        // 400000001-231
        String bikeNum = msg.substring(3, 9);
        String token = msg.substring(9, msg.length() - 1);
        int state = Integer.valueOf(msg.substring(msg.length() - 1));
        return new UnlockResultMsg(activityName, bikeNum, state, token);
    }

    /**
     * 拼接byte[]
     *
     * @param data1
     * @param data2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 获取一个随机的byte
     *
     * @return
     */
    public static byte getToken() {
        byte token = (byte) (random.nextInt(256) - 128);
        return token;
    }

    /**
     * 获取数据
     *
     * @param bikeNumber
     * @param bytes
     * @return
     */
    public static byte[] getBytesForAscii(String bikeNumber, byte... bytes) {
        byte[] bytesForBikeNumber = MsgUtil.str2bytesByAscii(bikeNumber);
        int length = bytes.length;
        byte[] bs = new byte[length];
        for (int i = 1; i < length; i++) {
            bs[i] = bytes[i];
        }
        byte[] addBytes = MsgUtil.addBytes(bytesForBikeNumber, bs);
        return addBytes;
    }

}
