package com.jny.common;

import android.content.Context;

import com.jny.android.demo.plugin.annotations.Plugin;

public interface PluginModulePlugin extends Plugin {
    void testPlugin();

    void startPluginActivity(Context context);
}
