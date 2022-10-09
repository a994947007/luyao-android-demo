package com.jny.android.demo.base_util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

public final class SystemServiceUtils {
    public static int getKeyboardHeight(Context context) {
        try {
            @SuppressLint("WrongConstant") InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService("input_method");
            @SuppressLint("DiscouragedPrivateApi") Method getInputMethodWindowVisibleHeightMethod = InputMethodManager.class.getDeclaredMethod("getInputMethodWindowVisibleHeight");
            getInputMethodWindowVisibleHeightMethod.setAccessible(true);
            Integer height = (Integer) getInputMethodWindowVisibleHeightMethod.invoke(inputMethodManager);
            return height == null ? -1 : height;
        } catch (Exception var3) {
            var3.printStackTrace();
            return -1;
        }
    }
}
