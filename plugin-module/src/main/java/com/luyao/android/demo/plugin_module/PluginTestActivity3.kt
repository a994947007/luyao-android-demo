package com.luyao.android.demo.plugin_module

import android.os.Bundle
import android.view.LayoutInflater

class PluginTestActivity3 : PluginBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat)
        super.onCreate(savedInstanceState)
        val contentView = LayoutInflater.from(mContext).inflate(R.layout.activity_plugin_test3, null)
        setContentView(contentView)
    }
}