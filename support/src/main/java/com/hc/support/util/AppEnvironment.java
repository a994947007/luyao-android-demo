package com.hc.support.util;

import android.content.Context;

public class AppEnvironment {
    private Context context;

    private static class Instance{
        private static final AppEnvironment instance = new AppEnvironment();
    }

    public static AppEnvironment getInstance() {
        return Instance.instance;
    }

    public static Context getAppContext() {
        return getInstance().context;
    }

    public static void setContext(Context context) {
        getInstance().context = context;
    }
}
