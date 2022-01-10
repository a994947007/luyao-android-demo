package com.hc.support.looper;

public class MyHandler {

    private final MyLooper looper;
    private MyCallback callback;

    public MyHandler(MyLooper looper) {
        this.looper = looper;
        if (looper == null) {
            throw new IllegalArgumentException("looper must not bean null");
        }
    }

    public MyHandler() {
        this.looper = MyLooper.myLooper();
        if (looper == null) {
            throw new IllegalArgumentException("looper must prepare");
        }
    }

    public MyHandler(MyCallback callback) {
        this();
        this.callback = callback;
    }

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    public boolean sendMessage(MyMessage message) {
        return sendMessageAtTime(message, System.currentTimeMillis());
    }

    public boolean sendMessageAtTime(MyMessage message, long time) {
        return enqueueMessage(looper.mQueue, message, time);
    }

    public boolean sendMessageDelay(MyMessage message, long time) {
        return sendMessageAtTime(message, System.currentTimeMillis() + time);
    }

    private boolean enqueueMessage(MyMessageQueue queue, MyMessage message, long time) {
        message.target = this;
        return queue.enqueueMessage(message, time);
    }

    public MyMessage removeMessage(int token) {
        return looper.mQueue.removeMessage(this, token);
    }

    void dispatchMessage(MyMessage message) {
        if (callback != null) {
            boolean done = callback.handleMessage(message);
            if (done) {
                message.recycle();
                return;
            }
        }
        handleMessage(message);
        message.recycle();
    }

    public void handleMessage(MyMessage message) { }

    public interface MyCallback {
        boolean handleMessage(MyMessage message);
    }
}
