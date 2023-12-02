package com.hc.android_demo.fragment.content.framework.activity;

import android.app.ActivityManager;
import android.content.Context;

import com.hc.util.ToastUtils;
import com.jny.android.demo.base_util.AppEnvironment;

import java.util.List;

public class ActivityTaskUtils {
    public static void toastActivityTask() {
        ActivityManager activityManager = (ActivityManager) AppEnvironment.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(10);
        String info = "";
        int i = 0;
        for (ActivityManager.RunningTaskInfo runningTask : runningTasks) {
            info += "task" + i++ + ": " + + runningTasks.size() + ",\n"
            + runningTask.topActivity.getShortClassName() + "\n";
        }
        ToastUtils.show(info);
    }
}
