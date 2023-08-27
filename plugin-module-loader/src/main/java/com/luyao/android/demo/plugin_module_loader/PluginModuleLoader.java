package com.luyao.android.demo.plugin_module_loader;

public class PluginModuleLoader {

    private static class Instance {
        private final static PluginModuleLoader pluginModuleLoader = new PluginModuleLoader();
    }

    public static PluginModuleLoader getInstance() {
        return Instance.pluginModuleLoader;
    }

    public void load(String moduleName, Callback callback) {

    }
}
