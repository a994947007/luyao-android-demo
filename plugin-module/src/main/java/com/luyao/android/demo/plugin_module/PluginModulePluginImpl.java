package com.luyao.android.demo.plugin_module;

import com.hc.util.ToastUtils;
import com.jny.android.demo.plugin.annotations.InjectModule;
import com.jny.common.PluginModulePlugin;

@InjectModule
public class PluginModulePluginImpl implements PluginModulePlugin {
    @Override
    public void testPlugin() {
        ToastUtils.show("PluginModule test plugin");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
