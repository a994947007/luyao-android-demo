package com.hc.android_demo;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hc.base.BaseApplication;
import com.hc.support.loadSir.LoadSir;
import com.jny.common.load.LoadingCallback;
import com.jny.common.load.PageErrorCallback;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Fresco.initialize(this);
        LoadSir.buildLoadSir()
                .addCallback(new LoadingCallback())
                .addCallback(new PageErrorCallback());
    }
}