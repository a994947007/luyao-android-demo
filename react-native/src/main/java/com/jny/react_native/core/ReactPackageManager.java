package com.jny.react_native.core;

import com.facebook.react.ReactPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ReactPackageManager {

    private static volatile ReactPackageManager mInstance = null;

    private final Map<String, LuReactPackage> mReactPackages = new HashMap<>();

    static {
        if (mInstance == null) {
            synchronized (ReactPackageManager.class) {
                if (mInstance == null) {
                    mInstance = new ReactPackageManager();
                }
            }
        }
    }

    public void registerAll() {
        ServiceLoader<LuReactPackage> reactPackages = ServiceLoader.load(LuReactPackage.class);
        for (LuReactPackage reactPackage : reactPackages) {
            register(reactPackage.getBundleId(), reactPackage);
        }
    }

    public static ReactPackageManager getInstance() {
        return mInstance;
    }

    public ReactPackage getReactPackage(String bundleId) {
        return mReactPackages.get(bundleId);
    }

    public void register(String bundleId, LuReactPackage reactPackage) {
        mReactPackages.put(bundleId, reactPackage);
    }

    public void unregister(String bundleId) {
        mReactPackages.remove(bundleId);
    }
}
