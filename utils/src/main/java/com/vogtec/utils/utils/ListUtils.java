package com.vogtec.utils.utils;

import java.util.List;

/**
 * Created by PC on 2016/12/15.
 */
public class ListUtils {
    public static int getMax(List<Integer> data) {
        int max = -1;
        for (Integer integer : data) {
            if (integer > max) {
                max = integer;
            }
        }
        return max;
    }

    public static int getMin(List<Integer> data) {
        int min = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) < data.get(i + 1)) {
                min = data.get(i);
            } else {
                min = data.get(i + 1);
            }
        }
        return min;
    }
}
