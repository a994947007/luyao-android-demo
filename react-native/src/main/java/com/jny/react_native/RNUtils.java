package com.jny.react_native;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public final class RNUtils {
    public static  WritableMap toWritableMap(Object o) throws IllegalAccessException {
        WritableMap resultMap = Arguments.createMap();
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(SerializedName.class)) {
                SerializedName annotation = declaredField.getAnnotation(SerializedName.class);
                String name = annotation.value();
                Class<?> type = declaredField.getType();
                if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
                    resultMap.putInt(name, declaredField.getInt(o));
                } else if (type.isAssignableFrom(String.class)) {
                    resultMap.putString(name, String.valueOf(declaredField.get(o)));
                } else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
                    resultMap.putDouble(name, declaredField.getDouble(o));
                } else if (type.isAssignableFrom(List.class)) {
                    WritableArray writableArray = Arguments.createArray();
                    Type t = declaredField.getGenericType();
                    Type[] actualTypeArguments = ((ParameterizedType) t).getActualTypeArguments();
                    Class<?> typeClass = (Class<?>)actualTypeArguments[0];
                    List<Object> args = (List<Object>) declaredField.get(o);
                    for (Object arg : args) {
                        if (typeClass.isAssignableFrom(Integer.class) || typeClass.isAssignableFrom(int.class)) {
                            writableArray.pushInt((Integer) arg);
                        } else if (typeClass.isAssignableFrom(String.class)) {
                            writableArray.pushString(String.valueOf(arg));
                        } else if (typeClass.isAssignableFrom(Double.class) || typeClass.isAssignableFrom(double.class)) {
                            writableArray.pushDouble((Double) arg);
                        } else {
                            WritableMap map = toWritableMap(arg);
                            writableArray.pushMap(map);
                        }
                    }
                    resultMap.putArray(name, writableArray);
                } else {
                    resultMap.putMap(name, toWritableMap(declaredField.get(o)));
                }
            }
        }
        return resultMap;
    }

    public static WritableMap toWritableMap(JsonObject jsonObject) {
        WritableMap map = Arguments.createMap();
        return map;
    }
}
