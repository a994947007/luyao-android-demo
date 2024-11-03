package com.hc.support.mvps;

import java.util.HashMap;
import java.util.Map;

public class ContextWrapper {
    private final Map<Class<?>, BeanWrapper<Object>> clazzCallerContext = new HashMap<>();
    private final Map<String, BeanWrapper<Object>> stringCallerContext = new HashMap<>();

    public void add(String key, Object bean) {
        if (stringCallerContext.containsKey(key)) {
            throw new IllegalArgumentException(key + " has bean injected");
        }
        stringCallerContext.put(key, () -> bean);
    }

    public void add(Class<?> clazz, Object bean) {
        if (clazzCallerContext.containsKey(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + " has bean injected");
        }
        clazzCallerContext.put(clazz, () -> bean);
    }

    public <T> Object get(Class<?> clazz) {
        BeanWrapper<Object> wrapper = clazzCallerContext.get(clazz);
        if (wrapper == null) {
            return null;
        }
        return (T)wrapper.get();
    }

    public <T> Object get(String key) {
        BeanWrapper<Object> wrapper = clazzCallerContext.get(key);
        if (wrapper == null) {
            return null;
        }
        return (T)wrapper.get();
    }
}
