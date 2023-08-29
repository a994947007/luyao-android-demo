package com.luyao.android.demo.plugin_module_loader;

import android.content.Context;
import android.util.Log;

import com.jny.android.demo.plugin.PluginInit;
import com.jny.android.demo.plugin.PluginUtils;

public class PluginLoader {
    private static final String TAG = "PluginLoader";

    public static void loadPlugin(Context context, String moduleName, Callback callback) {
        String modulePath = PluginModulePathMapper.getModulePath(moduleName);
        PluginModuleLoader.getInstance().load(context, modulePath, new Callback() {
            @Override
            public void onSuccess() {
                PluginInit modulePluginInit = PluginUtils.getModulePluginInit(moduleName);
                if (modulePluginInit != null) {
                    modulePluginInit.execute();
                    callback.onSuccess();
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError");
                callback.onError();
            }
        });
    }
}
