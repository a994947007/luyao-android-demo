package com.hc.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewConfiguration;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.hc.base.AppEnvironment;

public class ViewUtils {
    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
    private static float mFlingFriction = ViewConfiguration.getScrollFriction();

    private static double getSplineDeceleration(Context context, int i) {
        float mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        return Math.log((0.35f * ((float) Math.abs(i))) / (mFlingFriction * mPhysicalCoeff));
    }

    private static double getSplineDecelerationByDistance(Context context, double d) {
        float mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        return ((((double) DECELERATION_RATE) - 1.0d) * Math.log(d / ((double) (mFlingFriction * mPhysicalCoeff)))) / ((double) DECELERATION_RATE);
    }

    public static double getSplineFlingDistance(Context context, int i) {
        float mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        return Math.exp(getSplineDeceleration(context, i) * (((double) DECELERATION_RATE) / (((double) DECELERATION_RATE) - 1.0d))) * ((double) (mFlingFriction * mPhysicalCoeff));
    }

    public static int getVelocityByDistance(Context context, double d) {
        float mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        return Math.abs((int) (((Math.exp(getSplineDecelerationByDistance(context, d)) * ((double) mFlingFriction)) * ((double) mPhysicalCoeff)) / 0.3499999940395355d));
    }

    public static int getColor(@ColorRes int resId) {
        Context context = AppEnvironment.getInstance().getAppContext();
        // 同时兼容高、低版本
        return ContextCompat.getColor(context, resId);
    }

    public static String getString(@StringRes int resId) {
        Context context = AppEnvironment.getInstance().getAppContext();
        return context.getString(resId);
    }
}
