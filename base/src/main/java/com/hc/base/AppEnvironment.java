package com.hc.base;

import android.app.Application;
import android.content.Context;

public class AppEnvironment {
    private Context context;
    private Application application;

    private static class Instance{
        private static final AppEnvironment instance = new AppEnvironment();
    }

    public static AppEnvironment getInstance() {
        return Instance.instance;
    }

    public static Context getAppContext() {
        return getInstance().context;
    }

    public static Application getApplication() {
        return getInstance().application;
    }

    public static void setContext(Context context) {
        getInstance().context = context;
    }

    public static void setApplication(Application application) {
        getInstance().application = application;
    }
}
