package com.hc.util;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;

import com.hc.base.AppEnvironment;

public class UriUtils {
    public static String getUri(int resId) {
        Resources r = AppEnvironment.getAppContext().getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.toString();
    }
}
