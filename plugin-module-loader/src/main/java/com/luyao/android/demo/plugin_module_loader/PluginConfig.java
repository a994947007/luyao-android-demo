package com.luyao.android.demo.plugin_module_loader;

public class PluginConfig {
    public String moduleName;
    public String modulePath;
    public String modulePackage;

    public PluginConfig() {}

    public PluginConfig(String moduleName, String modulePath, String modulePackage) {
        this.moduleName = moduleName;
        this.modulePath = modulePath;
        this.modulePackage = modulePackage;
    }
}
