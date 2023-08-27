package com.jny.android.demo.plugin;

import com.jny.android.demo.plugin.annotations.Plugin;

import java.util.HashMap;
import java.util.Map;

public final class PluginCenter {

    private static final Map<Class<? extends Plugin>, Class<? extends Plugin>> mPluginClassMap = new HashMap<>();
    private static final Map<Class<? extends Plugin>, Plugin> mPluginMap = new HashMap<>();

    public static <T extends Plugin, V extends T> void register(Class<T> plugin, Class<V> pluginImpl) {
        mPluginClassMap.put(plugin, pluginImpl);
    }

    public static <T extends Plugin> T get(Class<T> pluginClass) {
        T pluginImpl = (T) mPluginMap.get(pluginClass);
        if (pluginImpl != null) {
            return pluginImpl;
        }
        Class<? extends Plugin> pluginImplClass = mPluginClassMap.get(pluginClass);
        if (pluginImplClass == null || !pluginClass.isAssignableFrom(pluginImplClass)) {
            return null;
        }
        try {
            pluginImpl = (T) pluginImplClass.newInstance();
            mPluginMap.put(pluginClass, pluginImpl);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return pluginImpl;
    }
}
