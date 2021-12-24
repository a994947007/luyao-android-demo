package com.hc.support.skin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import com.hc.support.skin.util.SkinThemeUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Observable;

public class ApplicationActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    private final Observable mObservable;
    private final HashMap<Activity, SkinLayoutInflaterFactory> mLayoutInflaterFactories = new HashMap<>();

    public ApplicationActivityLifeCycle(Observable observable) {
        this.mObservable = observable;
    }

    /** 任何Activity onCreate都会走这个回调 */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SkinThemeUtils.updateStatusBarColor(activity);
        }

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        try {
            @SuppressLint("SoonBlockedPrivateApi")
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
        mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);
        mObservable.addObserver(skinLayoutInflaterFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) { }

    @Override
    public void onActivityResumed(@NonNull Activity activity) { }

    @Override
    public void onActivityPaused(@NonNull Activity activity) { }

    @Override
    public void onActivityStopped(@NonNull Activity activity) { }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflaterFactory observer = mLayoutInflaterFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }
}
