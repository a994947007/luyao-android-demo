package com.hc.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.DrawableRes;

import com.hc.base.AppEnvironment;

public final class BitmapUtils {

    public static Bitmap getBitmap(@DrawableRes int resId) {
        return BitmapFactory.decodeResource(AppEnvironment.getAppContext().getResources(), resId);
    }

    public static Bitmap getBitmap(Resources resources, @DrawableRes int resId) {
        return BitmapFactory.decodeResource(resources, resId);
    }

    public static Bitmap getBitmap(Resources resources, @DrawableRes int resId, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

}
