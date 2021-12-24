package com.hc.support.skin;

import android.app.Application;

public class SkinPreference {

    private static class Instance {
        private static final SkinPreference sInstance;

        static {
            sInstance = new SkinPreference();
        }
    }

    public static SkinPreference getInstance() {
        return Instance.sInstance;
    }

    public void init(Application application) {

    }

    public void setSkinPath(String skinPath) {

    }

    public String getSkinPath() {
        return null;
    }

    public void reset() {

    }
}
