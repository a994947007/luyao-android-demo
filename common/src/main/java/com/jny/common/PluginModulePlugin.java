package com.jny.common;

import android.content.Context;

import com.jny.android.demo.plugin.annotations.Plugin;

public interface PluginModulePlugin extends Plugin {
    void testPlugin();

    void startPluginActivity(Context context);

    void startPluginActivity2(Context context);

    void startPluginActivity3(Context context);

    void startPluginActivity4(Context context);
}
