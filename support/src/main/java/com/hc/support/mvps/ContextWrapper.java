package com.hc.support.mvps;

import java.util.HashMap;
import java.util.Map;

public class ContextWrapper {
    private final Map<Class<?>, Object> clazzCallerContext = new HashMap<>();
    private final Map<String, Object> stringCallerContext = new HashMap<>();

    public void add(String key, Object bean) {
        if (stringCallerContext.containsKey(key)) {
            throw new IllegalArgumentException(key + " has bean injected");
        }
        stringCallerContext.put(key, bean);
    }

    public void add(Class<?> clazz, Object bean) {
        if (clazzCallerContext.containsKey(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + " has bean injected");
        }
        clazzCallerContext.put(clazz, bean);
    }

    public <T> Object get(Class<?> clazz) {
        return clazzCallerContext.get(clazz);
    }

    public <T> Object get(String key) {
        return stringCallerContext.get(key);
    }
}
