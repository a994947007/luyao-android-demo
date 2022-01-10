package com.hc.support.looper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public abstract class MyIntentService extends Service {

    private boolean mRedelivery;
    private MyLooper myServiceLooper;
    private MyHandler myServiceHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        MyHandlerThread myHandlerThread = new MyHandlerThread();
        myHandlerThread.start();
        myServiceLooper = myHandlerThread.getLooper();
        myServiceHandler = new MyHandler(myServiceLooper);
    }

    @Deprecated
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        myServiceLooper.quit();
    }

    public void setIntentRedelivery(boolean enabled) {
        this.mRedelivery = enabled;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final MyMessage msg = MyMessage.obtain();
        msg.arg1 = startId;
        msg.obj = intent;
        myServiceHandler.setCallback(new MyHandler.MyCallback() {
            @Override
            public boolean handleMessage(MyMessage message) {
                onHandlerIntent((Intent) message.obj);
                stopSelf(message.arg1);
                return true;
            }
        });
        myServiceHandler.sendMessage(msg);
        return mRedelivery? START_REDELIVER_INTENT: START_NOT_STICKY;
    }

    protected abstract void onHandlerIntent(@Nullable Intent intent);
}
