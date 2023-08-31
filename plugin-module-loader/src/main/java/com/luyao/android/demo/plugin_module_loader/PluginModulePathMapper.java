package com.luyao.android.demo.plugin_module_loader;

import java.util.HashMap;
import java.util.Map;

public class PluginModulePathMapper {
    private static final Map<String, String> modulePathMap = new HashMap<>();

    static {
        // 插件apk存放在当前目录
        modulePathMap.put("plugin-module", "/storage/emulated/0/plugin-module-debug.apk");
    }

    public static String getModulePath(String moduleName) {
        return modulePathMap.get(moduleName);
    }
}
