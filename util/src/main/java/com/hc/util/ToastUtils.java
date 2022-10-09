package com.hc.util;

import android.widget.Toast;

import com.jny.android.demo.base_util.AppEnvironment;

public final class ToastUtils {
    
    public static void show(String text, int type) {
        Toast.makeText(AppEnvironment.getAppContext(), text, type).show();
    }

    public static void show(String text) {
        show(text, Toast.LENGTH_SHORT);
    }
}
