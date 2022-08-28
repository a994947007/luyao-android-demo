package com.hc.android_demo.fragment.content.framework.util;

import androidx.annotation.StyleRes;

import com.hc.android_demo.R;

public class ThemeUtils {

    @StyleRes
    private int style = R.style.AppTheme;

    static class Instance{
        private static final ThemeUtils instance;
        static {
            instance = new ThemeUtils();
        }
    }

    private ThemeUtils() {}

    public static ThemeUtils getInstance() {
        return Instance.instance;
    }

    public void setStyle(@StyleRes int style) {
        this.style = style;
    }

    public int getStyle() {
        return this.style;
    }
}
