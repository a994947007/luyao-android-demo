package com.hc.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hc.base.activity.Constants;
import com.hc.base.activity.ContentActivity;
import com.hc.base.activity.LuActivity;
import com.hc.base.activity.StitchingActivity;

public final class ActivityUtils {
    public static void startContentActivity(LuActivity luActivity, String id) {
        Intent intent = new Intent(luActivity, ContentActivity.class);
        intent.putExtra(com.hc.base.activity.Constants.CONTENT_FRAGMENT_KEY, id);
        luActivity.startActivity(intent);
    }

    public static void startContentActivity(Context context, String id) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(com.hc.base.activity.Constants.CONTENT_FRAGMENT_KEY, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startStitchingActivity(LuActivity luActivity, String id, Bundle params) {
        Intent intent = new Intent(luActivity, StitchingActivity.class);
        intent.putExtras(params);
        intent.putExtra(com.hc.base.activity.Constants.CONTENT_FRAGMENT_KEY, id);
        luActivity.startActivity(intent);
        luActivity.overridePendingTransition(0,0);
    }
}
