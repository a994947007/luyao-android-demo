package com.luyao.android.demo.plugin_module

import android.app.Activity
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle

class PluginTestActivity4 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_test4)
    }

    override fun getResources(): Resources {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPathMethod =
            AssetManager::class.java.getMethod("addAssetPath", String::class.java)
        addAssetPathMethod.isAccessible = true
        addAssetPathMethod.invoke(assetManager, "/storage/emulated/0/plugin-module-debug.apk")

        val resources: Resources = application.resources
        return Resources(assetManager, resources.displayMetrics, resources.configuration)
    }
}