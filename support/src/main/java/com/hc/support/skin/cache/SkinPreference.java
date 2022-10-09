package com.hc.support.skin.cache;

import android.app.Application;

import com.jny.android.demo.rxandroid.function.Function;
import com.jny.android.demo.rxandroid.observable.Observable;
import com.jny.android.demo.rxandroid.schedule.Schedules;

import java.io.IOException;

public class SkinPreference {

    private LuDataBase mLuDataBase;

    private static class Instance {
        private static final SkinPreference sInstance;

        static {
            sInstance = new SkinPreference();
        }
    }

    public static SkinPreference getInstance() {
        return Instance.sInstance;
    }

    public void init(Application application) {
        if (mLuDataBase == null) {
            mLuDataBase = LuDataBase.create(application);
        }
    }

    public Observable<Boolean> setSkinPath(SkinConfig skinConfig) {
        return Observable.just(skinConfig)
            .map(new Function<SkinConfig, Boolean>() {
                @Override
                public Boolean apply(SkinConfig skinConfig) {
                    mLuDataBase.skinConfigDao().insert(skinConfig);
                    return true;
                }
            }).subscribeOn(Schedules.IO);

    }

    public Observable<SkinConfig> getSkinPath(final String skinKey) {
        return Observable.just(skinKey)
                .map(new Function<String, SkinConfig>() {
                    @Override
                    public SkinConfig apply(String s) throws IOException {
                        SkinConfig skinConfig = mLuDataBase.skinConfigDao().getSkinConfigByKey(skinKey);
                        if (skinConfig == null) {
                            skinConfig = new SkinConfig();
                        }
                        return skinConfig;
                    }
                }).subscribeOn(Schedules.IO);
    }

    public Observable<Boolean> deleteSkinConfig(String skinKey) {
        return Observable.just(skinKey)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String key) {
                        mLuDataBase.skinConfigDao().deleteSkinConfig(key);
                        return true;
                    }
                }).subscribeOn(Schedules.IO);

    }
}
