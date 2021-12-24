package com.hc.support.skin.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.hc.support.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SkinThemeUtils {

    private static final int [] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
        R.attr.colorPrimaryDark
    };

    private static final int [] STATUS_BAR_COLOR_ATTRS = {
            android.R.attr.statusBarColor, android.R.attr.navigationBarColor
    };

    public static void updateStatusBarColor(Activity activity) {
        // android5.0以上才能修改状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        int [] resIds = getResId(activity, STATUS_BAR_COLOR_ATTRS);
        int statusBarColorResId = resIds[0];
        int navigationBarColor = resIds[1];

        int colorPrimaryDarkResId = 0;
        // statusBar
        if (statusBarColorResId != 0) {
            int color = SkinResources.getInstance().getColor(statusBarColorResId);
            activity.getWindow().setStatusBarColor(color);
        } else {
            colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if (colorPrimaryDarkResId != 0) {   // 设置status bar为主题色
                int color = SkinResources.getInstance().getColor(colorPrimaryDarkResId);
                activity.getWindow().setStatusBarColor(color);
            }
        }

        // navigationBar
        if (navigationBarColor != 0) {
            int color = SkinResources.getInstance().getColor(navigationBarColor);
            activity.getWindow().setNavigationBarColor(color);
        } else {
            if (colorPrimaryDarkResId == 0) {
                colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            }
            if (colorPrimaryDarkResId != 0) {   // navigation bar为主题色
                int color = SkinResources.getInstance().getColor(colorPrimaryDarkResId);
                activity.getWindow().setNavigationBarColor(color);
            }
        }
    }

    public static int [] getResId(Context context, int [] attrs) {
        int [] resIds = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < attrs.length; i++) {
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }
}
