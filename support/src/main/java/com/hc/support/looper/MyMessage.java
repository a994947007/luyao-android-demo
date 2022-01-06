package com.hc.support.looper;

public class MyMessage {
    MyMessage next;
    long handleTime;
    MyHandler target;
    int token = -1;

    private static MyMessage sPool;    // 内存池
    private static final int MAX_POOL_SIZE = 50;
    private static int sPoolSize = 0;
    private boolean mIsAsync;

    public MyMessage() {

    }

    // Message复用
    public static MyMessage obtain() {
        MyMessage message = sPool;
        if (message != null) {
            sPool = message.next;
            message.next = null;
            sPoolSize --;
            return message;
        }
        return new MyMessage();
    }

    public void recycle() {
        next = null;
        handleTime = 0;
        target = null;
        token = -1;
        if (sPoolSize < MAX_POOL_SIZE) {
            if (sPool == null) {
                sPool = this;
            } else {
                this.next = sPool.next;
                sPool.next = this;
            }
            sPoolSize ++;
        }
    }

    public boolean isAsync() {
        return mIsAsync;
    }

    public void setAsync(boolean isAsync) {
        this.mIsAsync = isAsync;
    }
}
