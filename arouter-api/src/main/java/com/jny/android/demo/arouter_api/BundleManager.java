package com.jny.android.demo.arouter_api;

import android.app.Activity;
import android.util.LruCache;

import com.jny.android.demo.ProcessorConfig;
import com.jny.android.demo.api.ParameterLoad;

public class BundleManager {
    private LruCache<String, ParameterLoad> mParamLoadClassMap;

    private static volatile BundleManager mInstance = null;

    static {
        if (mInstance == null) {
            synchronized (BundleManager.class) {
                if (mInstance == null) {
                    mInstance = new BundleManager();
                }
            }
        }
    }

    public static BundleManager getInstance() {
        return mInstance;
    }

    private BundleManager() {
        mParamLoadClassMap = new LruCache<>(100);
    }

    public void bind(Activity activity) {
        String className = activity.getClass().getName() + ProcessorConfig.PARAMETER_LOADER_CLASS_NAME;
        ParameterLoad parameterLoad = mParamLoadClassMap.get(className);
        try {
            if (parameterLoad == null) {
                Class<?> parameterLoadClass = Class.forName(className);
                parameterLoad = (ParameterLoad) parameterLoadClass.newInstance();
                mParamLoadClassMap.put(className, parameterLoad);
            }
            parameterLoad.load(activity);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
