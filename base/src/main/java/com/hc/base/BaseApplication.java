package com.hc.base;

import android.app.Application;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEnvironment.setContext(getApplicationContext());
    }
}
