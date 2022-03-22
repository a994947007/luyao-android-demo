package com.hc.android_demo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hc.support.loadSir.LoadSir;
import com.jny.common.load.LoadingCallback;
import com.jny.common.load.PageErrorCallback;
import com.jny.react_native.MyReactApplication;

public class MyApplication extends MyReactApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Fresco.initialize(this);
        LoadSir.buildLoadSir()
                .addCallback(new LoadingCallback())
                .addCallback(new PageErrorCallback());

        // 解决多个进程访问同一个web路径的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            String packageName = this.getPackageName();
            if (!packageName.equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }


    private String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}