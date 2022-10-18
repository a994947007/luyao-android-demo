package com.jny.android.demo.arouter_api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.LruCache;

import com.jny.android.demo.ProcessorConfig;
import com.jny.android.demo.RouterBean;
import com.jny.android.demo.api.ARouterGroup;
import com.jny.android.demo.api.ARouterPath;
import com.jny.android.demo.api.IRouterTab;

public class RouterManager {

    private LruCache<String, RouterBean> mRouterMap;

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
        mRouterMap = new LruCache<>(100);
    }

    public void nav(Context context, Bundle bundle, String moduleName, String path) {
        if (path == null || "".equals(path)) {
            throw new IllegalArgumentException();
        }

        RouterBean routerBean = mRouterMap.get(path);
        if (routerBean == null) {
            routerBean = loadRouterBean(path);
            mRouterMap.put(path, routerBean);
        }
        Intent intent = new Intent(context, routerBean.getMyClass());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void nav(Context context, String path, String moduleName, BundleBuilder builder) {
        nav(context, builder.build(), moduleName, path);
    }

    public RouterBean loadRouterBean(String path) {
        String group;
        String fullPath = path;

        if (path.lastIndexOf("/") != 0) {
            if (path.startsWith("/")) {
                group = path.substring(1, path.indexOf("/", 1));
            } else {
                group = path.substring(0, path.indexOf("/", 1));
            }
        } else {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            group = ProcessorConfig.DEFAULT_GROUP;
            fullPath = "/" + ProcessorConfig.DEFAULT_GROUP + "/" + path;
        }

        String routerTabClassName = ProcessorConfig.ROUTER_TAB_CLASS_PACKAGE + "."
                + String.valueOf(group.charAt(0)).toUpperCase() + group.substring(1)
                + ProcessorConfig.ROUTER_TAB_CLASS;

        try {
            Class<?> routerTabClass = Class.forName(routerTabClassName);
            IRouterTab routerTab = (IRouterTab) routerTabClass.newInstance();
            String aRouterGroupClassName = routerTab.getRouterTab().get(fullPath);
            if (aRouterGroupClassName != null) {
                aRouterGroupClassName += "." + ProcessorConfig.PREFIX_GROUP_CLASS_NAME
                        + String.valueOf(group.charAt(0)).toUpperCase()
                        + group.substring(1);
            }
            Class<?> aRouterGroupClass = Class.forName(aRouterGroupClassName);
            ARouterGroup loadGroup = (ARouterGroup) aRouterGroupClass.newInstance();
            Class<? extends ARouterPath> aRouterPathClass = loadGroup.getGroupMap().get(group);
            ARouterPath aRouterPath = aRouterPathClass.newInstance();
            return aRouterPath.getPathMap().get(fullPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
