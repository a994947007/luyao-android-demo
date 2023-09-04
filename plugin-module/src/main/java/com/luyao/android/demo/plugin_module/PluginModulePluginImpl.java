package com.luyao.android.demo.plugin_module;

import android.content.Context;
import android.content.Intent;

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
    public void startPluginActivity(Context context) {
        context.startActivity(new Intent(context, PluginTestActivity.class));
    }

    @Override
    public void startPluginActivity2(Context context) {
        context.startActivity(new Intent(context, PluginTestActivity2.class));
    }

    @Override
    public void startPluginActivity3(Context context) {
        context.startActivity(new Intent(context, PluginTestActivity3.class));
    }

    @Override
    public void startPluginActivity4(Context context) {
        context.startActivity(new Intent(context, PluginTestActivity4.class));
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
