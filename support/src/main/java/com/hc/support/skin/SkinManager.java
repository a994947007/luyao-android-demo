package com.hc.support.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.jny.android.demo.rxandroid.observer.Consumer;
import com.hc.support.skin.cache.SkinConfig;
import com.hc.support.skin.cache.SkinPreference;
import com.hc.support.skin.util.SkinResources;
import com.hc.support.util.TextUtils;
import com.jny.core.DownloadCallback;
import com.jny.process.DownloadCenter;
import com.jny.process.DownloadDisposable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SkinManager extends java.util.Observable {

    private static final String TAG = "SkinManager";

    private static final String DEFAULT_SKIN_KEY = "DEFAULT_SKIN_KEY";

    private static final String PATH = "/skinPatch";

    private static volatile SkinManager sInstance = null;

    private final Application application;

    private SkinManager(Application application) {
        this.application = application;
        initSkin();
    }

    public void preloadSkinPatch() {
        SkinPreference.getInstance().init(application);
        DownloadDisposable ignore = DownloadCenter.getInstance().download(PATH, new DownloadCallback() {
            @Override
            public void onStart() { }

            @Override
            public void onDownload(float progress) { }

            @Override
            public void onSuccess(String resultPath) {
                SkinPreference.getInstance().setSkinPath(new SkinConfig(DEFAULT_SKIN_KEY, resultPath)).subscribe();
            }

            @Override
            public void onError(Throwable r) {
                Log.e(TAG, r.getMessage());
            }
        });
    }

    private void initSkin() {
        SkinPreference.getInstance().init(application);
        SkinResources.getInstance().init(application);
        ApplicationActivityLifeCycle applicationActivityLifeCycle = new ApplicationActivityLifeCycle(this);
        application.registerActivityLifecycleCallbacks(applicationActivityLifeCycle);
        SkinPreference.getInstance().getSkinPath(DEFAULT_SKIN_KEY)
            .subscribe(new Consumer<SkinConfig>() {
                @Override
                public void accept(SkinConfig skinConfig) {
                    if (!TextUtils.isEmpty(skinConfig.mSkinPath)) {
                        loadSkin(skinConfig.mSkinPath);
                    }
                }
            });
    }

    public void loadSkin(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            SkinPreference.getInstance().deleteSkinConfig(DEFAULT_SKIN_KEY).subscribe();
            SkinResources.getInstance().reset();
        } else {
            Resources appResource = application.getResources();
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager, skinPath);

                Resources skinResource = new Resources(assetManager, appResource.getDisplayMetrics(), appResource.getConfiguration());
                PackageManager pm = application.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = packageInfo.packageName;
                SkinResources.getInstance().applySkin(skinResource, packageName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        }
    }

    public static SkinManager init(Application application) {
        if (sInstance == null) {
            synchronized (SkinManager.class) {
                if (sInstance == null) {
                    sInstance = new SkinManager(application);
                }
            }
        }
        return sInstance;
    }

    public static SkinManager getInstance() {
        return sInstance;
    }
}
