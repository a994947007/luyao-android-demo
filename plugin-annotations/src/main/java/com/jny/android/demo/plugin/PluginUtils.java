package com.jny.android.demo.plugin;

import com.jny.android.demo.plugin.annotations.ProcessorConfigV2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class PluginUtils {
    public static String makeModuleClassName(String moduleName) {
        String name = moduleName.replace("-", "");
        return ("" + name.charAt(0)).toUpperCase() + name.substring(1) + "PluginInitImpl";
    }

    public static PluginInit getModulePluginInit(String moduleName) {
        String className = makeModuleClassName(moduleName);
        try {
            Class<?> moduleClass = Class.forName(ProcessorConfigV2.DEFAULT_PLUGIN_RESULT_PATH + "." + className);
            Constructor<?> constructor = moduleClass.getConstructor();
            constructor.setAccessible(true);
            return (PluginInit) constructor.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
