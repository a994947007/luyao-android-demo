package com.hc.android_demo.fragment.content.service;

import android.view.WindowManager;

import java.lang.reflect.Field;

public class FloatUtils {
    public static void disableMoveAnim(WindowManager.LayoutParams layoutParams) {
        try {
            Field privateFlagsField = WindowManager.LayoutParams.class.getField("privateFlags");
            Field field = WindowManager.LayoutParams.class.getField("PRIVATE_FLAG_NO_MOVE_ANIMATION");

            int value = (int) privateFlagsField.get(layoutParams);
            value |= (int)field.get(null);
            privateFlagsField.set(layoutParams, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }
    }
}
