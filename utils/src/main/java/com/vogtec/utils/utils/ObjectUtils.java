package com.vogtec.utils.utils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/10/11.
 */

public class ObjectUtils {
    /**
     * 将对象中的属性转换成Map集合
     */
    public static Map<String, String> objectToMap(Object object) {
        if (object == null) {
            return Collections.emptyMap();
        }

        Class<?> aClass = object.getClass();
        Class<?> superclass = aClass.getSuperclass();

        Field[] fields = aClass.getDeclaredFields();
        Field[] superclassFields = superclass.getDeclaredFields();

        Map<String, String> params = new HashMap<String, String>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = String.valueOf(field.get(object));
                if (value != null) {
                    params.put(name, value);
                }
            }

            for (Field field : superclassFields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = String.valueOf(field.get(object));
                if (value != null) {
                    params.put(name, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;

    }
}
