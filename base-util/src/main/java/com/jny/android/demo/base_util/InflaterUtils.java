package com.jny.android.demo.base_util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;

public final class InflaterUtils {
    public static <T extends View> T inflate(Activity activity, @LayoutRes int layoutId) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View inflateView = LayoutInflater.from(activity).inflate(layoutId, decorView, false);
        ViewGroup.LayoutParams layoutParams = inflateView.getLayoutParams();
        layoutParams.width = decorView.getMeasuredWidth();
        inflateView.setLayoutParams(layoutParams);
        return (T) inflateView;
    }

    public static View inflate(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }
}
