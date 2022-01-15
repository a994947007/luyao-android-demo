package com.hc.util;

import android.content.Intent;

import com.hc.base.activity.Constants;
import com.hc.base.activity.ContentActivity;
import com.hc.base.activity.LuActivity;

public final class ActivityUtils {
    public static void startContentActivity(LuActivity luActivity, String id) {
        Intent intent = new Intent(luActivity, ContentActivity.class);
        intent.putExtra(com.hc.base.activity.Constants.CONTENT_FRAGMENT_KEY, id);
        luActivity.startActivity(intent);
    }
}
