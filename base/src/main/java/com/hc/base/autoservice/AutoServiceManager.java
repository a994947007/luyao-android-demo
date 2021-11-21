package com.hc.base.autoservice;

import java.util.ServiceLoader;

public class AutoServiceManager {
    public static <T> T load(Class<T> clazz) {
        return clazz.cast(ServiceLoader.load(clazz).iterator().next());
    }
}
