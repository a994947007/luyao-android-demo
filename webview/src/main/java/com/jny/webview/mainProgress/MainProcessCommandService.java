package com.jny.webview.mainProgress;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainProcessCommandService extends Service {
    public MainProcessCommandService() {
    }

    /**
     * AIDL的Stub实现了IBinder接口，我们可以通过这种方式来直接调用另外一个进程的方法
     */
    @Override
    public IBinder onBind(Intent intent) {
        return MainProcessCommandsManager.getInstance();
    }
}