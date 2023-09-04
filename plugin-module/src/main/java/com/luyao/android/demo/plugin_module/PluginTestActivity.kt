package com.luyao.android.demo.plugin_module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class PluginTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_test)
        Log.d("PluginTestActivity", "onCreate");
    }
}