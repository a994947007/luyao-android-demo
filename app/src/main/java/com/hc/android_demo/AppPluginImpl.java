package com.hc.android_demo;

import com.jny.android.demo.plugin.annotations.InjectModule;
import com.jny.common.AppPlugin;

@InjectModule
public class AppPluginImpl implements AppPlugin {
    @Override
    public boolean testPlugin() {
        return true;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
