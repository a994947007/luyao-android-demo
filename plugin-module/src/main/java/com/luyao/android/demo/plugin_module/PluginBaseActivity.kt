package com.luyao.android.demo.plugin_module

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity

open class PluginBaseActivity : AppCompatActivity() {
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPathMethod =
            AssetManager::class.java.getMethod("addAssetPath", String::class.java)
        addAssetPathMethod.isAccessible = true
        addAssetPathMethod.invoke(assetManager, "/storage/emulated/0/plugin-module-debug.apk")

        val resources: Resources = application.resources
        val res = Resources(assetManager, resources.displayMetrics, resources.configuration)
        mContext = ContextThemeWrapper(baseContext, 0)
        val field = mContext.javaClass.getDeclaredField("mResources")
        field.isAccessible = true
        field.set(mContext, res)
    }
}