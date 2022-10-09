package com.hc.support.skin.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.jny.android.demo.base_util.TextUtils;


public class SkinResources {

    // 皮肤的路径
    private String mSkinPkgName;
    // 是否是默认皮肤
    private boolean isDefaultSkin = true;

    // app原始的resource
    private Resources mAppResources;
    // 皮肤白的resource
    private Resources mSkinResources;

    private static class Instance {
        private static final SkinResources skinResources;
        static {
            skinResources = new SkinResources();
        }
    }

    public static SkinResources getInstance() {
        return Instance.skinResources;
    }

    public void init(Application application) {
        mAppResources = application.getResources();
    }

    public void reset() {
        mSkinResources = null;
        mSkinPkgName = TextUtils.emptyString();
        isDefaultSkin = true;
    }

    public void applySkin(Resources resources, String pkgName) {
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    /**
     * 1. 通过原始app中的resId（@color/xxx）获取到名字，类型
     * 2. 根据名字类型在资源包中获取到对应的resId
     * R.color.Red(100001) -> Color/Red -> R.color.Red(20013)
     */
    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        return mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
    }

    /**
     * 根据Id获取到资源包里面对应的color
     */
    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(resId);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(resId);
    }

    /** 获取background */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);
        if ("color".equals(resourceTypeName)) {
            return getColor(resId);
        } else {
            return getDrawable(resId);
        }
    }
}
