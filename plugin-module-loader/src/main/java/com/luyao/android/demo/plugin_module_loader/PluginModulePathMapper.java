package com.luyao.android.demo.plugin_module_loader;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PluginModulePathMapper {
    private static final Map<String, PluginConfig> modulePathMap = new HashMap<>();

    static {
        // 插件apk存放在当前目录
        modulePathMap.put("plugin-module", new PluginConfig("plugin-module",
                "/storage/emulated/0/plugin-module-debug.apk",
                "com.luyao.android.demo.plugin_module"));
    }

    public static String getModulePath(String moduleName) {
        return Objects.requireNonNull(modulePathMap.get(moduleName)).modulePath;
    }

    public static String getModulePackage(String moduleName) {
        return Objects.requireNonNull(modulePathMap.get(moduleName)).modulePackage;
    }

    public static boolean contains(String className) {
        for (PluginConfig value : modulePathMap.values()) {
            if (className.startsWith(value.modulePackage)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static PluginConfig getPluginConfig(String className) {
        for (PluginConfig value : modulePathMap.values()) {
            if (className.startsWith(value.modulePackage)) {
                return value;
            }
        }
        return null;
    }
}
