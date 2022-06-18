package com.hc.util;

import android.os.Handler;
import android.os.Looper;

public final class Utils {

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUITread(Runnable runnable) {
        MAIN_HANDLER.post(runnable);
    }
}
