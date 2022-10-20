package com.jny.android.demo.arouter_api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.LruCache;

import com.jny.android.demo.ProcessorConfig;
import com.jny.android.demo.ProcessorUtils;
import com.jny.android.demo.RouterBean;
import com.jny.android.demo.RouterGroupModule;
import com.jny.android.demo.api.ARouterGroup;
import com.jny.android.demo.api.ARouterPath;
import com.jny.android.demo.api.IRouterTab;

public class RouterManager {

    private LruCache<String, RouterBean> mRouterMap;
    private LruCache<String, IRouterTab> mRouterTabMap;
    private LruCache<String, ARouterGroup> mRouterGroupMap;
    private LruCache<String, ARouterPath> mRouterPathMap;

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
        mRouterTabMap = new LruCache<>(20);
        mRouterGroupMap = new LruCache<>(50);
        mRouterPathMap = new LruCache<>(100);
    }

    public void nav(Context context, Bundle bundle, String path) {
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

    public void nav(Context context, String path,  BundleBuilder builder) {
        nav(context, builder.build(), path);
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
            IRouterTab routerTab = mRouterTabMap.get(routerTabClassName);
            if (routerTab == null) {
                Class<?> routerTabClass = Class.forName(routerTabClassName);
                routerTab = (IRouterTab) routerTabClass.newInstance();
                mRouterTabMap.put(routerTabClassName, routerTab);
            }

            RouterGroupModule routerGroupModule = routerTab.getRouterTab().get(fullPath);
            String aRouterGroupClassName = routerGroupModule.modulePath;
            if (aRouterGroupClassName != null) {
                aRouterGroupClassName += "." + ProcessorConfig.PREFIX_GROUP_CLASS_NAME
                        + ProcessorUtils.upperCaseFirstChat(routerGroupModule.moduleName);
            }

            ARouterGroup loadGroup = mRouterGroupMap.get(aRouterGroupClassName);
            if (loadGroup == null) {
                Class<?> aRouterGroupClass = Class.forName(aRouterGroupClassName);
                loadGroup = (ARouterGroup) aRouterGroupClass.newInstance();
                mRouterGroupMap.put(aRouterGroupClassName, loadGroup);
            }

            ARouterPath aRouterPath = mRouterPathMap.get(path);
            if (aRouterPath == null) {
                Class<? extends ARouterPath> aRouterPathClass = loadGroup.getGroupMap().get(group);
                aRouterPath = aRouterPathClass.newInstance();
                mRouterPathMap.put(path, aRouterPath);
            }

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
