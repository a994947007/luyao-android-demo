package com.hc.support.skin;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.hc.support.skin.util.SkinResources;
import com.hc.support.skin.util.SkinThemeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有需要换肤的对象
 */
public class SkinAttribute {

    /**
     * 记录view上需要换肤的属性
     */
    public void look(View view, AttributeSet attributeSet) {
        List<SkinPair> skinPairs = new ArrayList<>();
        int attributeCount = attributeSet.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attrName = attributeSet.getAttributeName(i);
            if (mAttributes.contains(attrName)) {
                String attrValue = attributeSet.getAttributeValue(i);
                if (attrValue.startsWith("#")) {
                    continue;
                }
                int resId = 0;
                if (attrValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attrValue.substring(1));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        resId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                    }
                } else {
                    resId = Integer.parseInt(attrValue.substring(1));
                }
                SkinPair skinPair = new SkinPair(attrName, resId);
                skinPairs.add(skinPair);
            }
        }
        mSkinViews.add(new SkinView(view, skinPairs));
    }

    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin();
        }
    }

    // 记录所有需要换肤的属性
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    private final List<SkinView> mSkinViews = new ArrayList<>();

    /**
     * 一个View包含多个属性
     */
    static class SkinView {
        View view;

        List<SkinPair> skinPairs;

        public SkinView(View view, List<SkinPair> skinPairs) {
            this.view = view;
            this.skinPairs = skinPairs;
        }

        public void applySkin() {
            applySkinSupport(); // 自定义View
            for (SkinPair skinPair : skinPairs) {
                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinPair.attrName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            // color
                            view.setBackgroundColor((Integer) background);
                        } else {
                            // drawable
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                        } else {
                            ((ImageView)view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView)view).setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId);
                        break;
                }
                if (left != null || top != null || right != null || bottom != null) {
                    ((TextView)view).setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
                }
            }
        }

        protected void applySkinSupport() {
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport)view).applySkin();
            }
        }
    }


    /**
     * 保存属性名和Id的对象
     */
    static class SkinPair {

        public SkinPair(String attrName, int resId) {
            this.attrName = attrName;
            this.resId = resId;
        }

        /**
         * 属性名 android:background
         */
        String attrName;

        /**
         * 属性Id @drawable/abc_vector_test
         */
        int resId;
    }
}
