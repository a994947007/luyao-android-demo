package com.jny.android.demo.plugin;

import com.jny.android.demo.plugin.annotations.Plugin;
import com.jny.android.demo.plugin.annotations.PluginClassGetter;
import com.jny.android.demo.plugin.annotations.ProcessorConfig;

import java.util.Map;

public class PluginManager {

    private static Map<Class<? extends Plugin>, Class<? extends Plugin>> mPluginClassMap;

    static {
        String className = ProcessorConfig.DEFAULT_PLUGIN_RESULT_PATH + "." + ProcessorConfig.GET_PLUGIN_CLASS_NAME;
        try {
            Class<?> clazz = Class.forName(className);
            PluginClassGetter getter = (PluginClassGetter) clazz.newInstance();
            mPluginClassMap = getter.getPluginMap();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Plugin> T get(Class<T> clazz) {
        if (mPluginClassMap == null) {
            return null;
        }
        Class<? extends Plugin> pluginImplClazz = mPluginClassMap.get(clazz);
        try {
            Plugin plugin = pluginImplClazz.newInstance();
            return (T) plugin;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
