package com.hc.support.looper;

public class MyLooper {

    private static final ThreadLocal<MyLooper> myLooperThreadLocal = new ThreadLocal<>();
    final MyMessageQueue mQueue = new MyMessageQueue();

    public static void prepare() {
        MyLooper looper = myLooper();
        if (looper != null) {
            throw new RuntimeException("Main Looper has bean init");
        }
        myLooperThreadLocal.set(new MyLooper());
    }

    public static void loop() {
        MyLooper looper = myLooper();
        if (looper == null) {
            throw new RuntimeException("looper is not init");
        }
        for (;;){
            MyMessage message = looper.mQueue.next();
            if (message == null) {
                return;
            }
            message.target.dispatchMessage(message);
        }
    }

    public void quit() {
        mQueue.quit();
    }

    public static MyLooper myLooper() {
        return myLooperThreadLocal.get();
    }
}
