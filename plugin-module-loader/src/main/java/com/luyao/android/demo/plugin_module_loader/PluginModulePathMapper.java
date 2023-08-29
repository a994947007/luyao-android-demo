package com.luyao.android.demo.plugin_module_loader;

import java.util.HashMap;
import java.util.Map;

public class PluginModulePathMapper {
    private static final Map<String, String> modulePathMap = new HashMap<>();

    static {
        modulePathMap.put("plugin-module", "\\sdcard\\plugin-module-debug.apk");
    }

    public static String getModulePath(String moduleName) {
        return modulePathMap.get(moduleName);
    }
}
