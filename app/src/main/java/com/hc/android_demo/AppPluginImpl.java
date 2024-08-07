package com.hc.android_demo;

import com.hc.util.ToastUtils;
import com.jny.android.demo.plugin.annotations.InjectModule;
import com.jny.common.AppPlugin;

@InjectModule
public class AppPluginImpl implements AppPlugin {
    @Override
    public boolean testPlugin() {
        ToastUtils.show("AppPlugin");
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
