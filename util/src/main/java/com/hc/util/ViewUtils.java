package com.hc.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.jny.android.demo.base_util.AppEnvironment;


public class ViewUtils {
    public static int dp2px(float dp) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f * (dp >= 0 ? 1 : -1));
    }

    public static int sp2px(float dp) {
        float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int)(dp * scale + 0.5f * (dp >= 0 ? 1 : -1));
    }

    public static int px2dp(float px) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f * (px >= 0 ? 1 : -1));
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

    public static int getStatusBarHeight() {
        Resources resources = AppEnvironment.getAppContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getNavigationBarHeight() {
        Resources resources = AppEnvironment.getAppContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    // displayHeight = screenHeight - statusBarHeight - navigationBarHeight
    public static int getDisplayHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static void addOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener globalListener) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        observer.removeOnGlobalLayoutListener(this);
                        globalListener.onGlobalLayout();
                    }
                }
        );
    }
}
