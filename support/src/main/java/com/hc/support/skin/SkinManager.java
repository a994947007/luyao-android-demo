package com.hc.support.skin;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.hc.support.skin.util.SkinResources;
import com.hc.support.util.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

public class SkinManager extends Observable {

    private static volatile SkinManager sInstance = null;

    private final Application application;

    private SkinManager(Application application) {
        this.application = application;
        initSkin();
    }

    private void initSkin() {
        SkinPreference.getInstance().init(application);
        SkinResources.getInstance().init(application);
        ApplicationActivityLifeCycle applicationActivityLifeCycle = new ApplicationActivityLifeCycle(this);
        application.registerActivityLifecycleCallbacks(applicationActivityLifeCycle);
        loadSkin(SkinPreference.getInstance().getSkinPath());
    }

    public void loadSkin(String skinPath) {
        if (TextUtils.isEmpty(skinPath)) {
            SkinPreference.getInstance().reset();
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

                SkinPreference.getInstance().setSkinPath(skinPath);
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
