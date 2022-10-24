package com.jny.android.demo.arouter_api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.LruCache;

import com.jny.android.demo.RouterBean;
import com.jny.android.demo.RouterBeanLoader;

public class RouterManager {

    private final LruCache<String, RouterBean> mRouterMap;

    private static volatile RouterManager mInstance = null;

    static {
        if (mInstance == null) {
            synchronized (RouterManager.class) {
                if (mInstance == null) {
                    mInstance = new RouterManager();
                }
            }
        }
    }

    public static RouterManager getInstance() {
        return mInstance;
    }

    private RouterManager() {
        mRouterMap = new LruCache<>(200);
    }

    public void nav(Context context, Bundle bundle, String path) {
        if (path == null || "".equals(path)) {
            throw new IllegalArgumentException();
        }

        RouterBean routerBean = mRouterMap.get(path);
        if (routerBean == null) {
            routerBean = RouterBeanLoader.getInstance().loadRouterBean(path);
            mRouterMap.put(path, routerBean);
        }
        Intent intent = new Intent(context, routerBean.getMyClass());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void nav(Context context, String path,  BundleBuilder builder) {
        nav(context, builder.build(), path);
    }
}
