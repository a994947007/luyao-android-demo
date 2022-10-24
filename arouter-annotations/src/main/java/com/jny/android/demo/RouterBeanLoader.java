package com.jny.android.demo;

import com.jny.android.demo.api.ARouterGroup;
import com.jny.android.demo.api.ARouterPath;
import com.jny.android.demo.api.IRouterTab;

import java.util.HashMap;
import java.util.Map;

public class RouterBeanLoader {

    private static volatile RouterBeanLoader instance;

    private final Map<String, IRouterTab> mRouterTabMap;
    private final Map<String, ARouterGroup> mRouterGroupMap;
    private final Map<String, ARouterPath> mRouterPathMap;

    static {
        if (instance == null) {
            synchronized (RouterBeanLoader.class) {
                if (instance == null) {
                    instance = new RouterBeanLoader();
                }
            }
        }
    }

    public static RouterBeanLoader getInstance() {
        return instance;
    }

    private RouterBeanLoader() {
        mRouterTabMap = new HashMap<>(20);
        mRouterGroupMap = new HashMap<>(50);
        mRouterPathMap = new HashMap<>(100);
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
