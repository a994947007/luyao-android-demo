package com.jny.android.demo.plugin.annotations;

import java.util.Map;

public interface PluginClassGetter {
    Map<Class<? extends Plugin>, Class<? extends Plugin>> getPluginMap();
}
