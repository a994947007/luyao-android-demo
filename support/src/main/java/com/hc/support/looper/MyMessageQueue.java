package com.hc.support.looper;

public class MyMessageQueue {

    // 实现一个带头结点的队列
    private final MyMessage queueHeader = new MyMessage();
    private boolean isQuit = false;
    private int mNextBarrierToken;

    // 在android中直接调用linux内核的wait notify，没有走java层
    public boolean enqueueMessage(MyMessage message, long time) {
        MyMessage preMessage = queueHeader;
        message.handleTime = time;
        synchronized (this) {
            if (isQuit) {
                return false;
            }
            while (preMessage.next != null && message.handleTime >= preMessage.next.handleTime) {
                preMessage = preMessage.next;
            }
            message.next = preMessage.next;
            preMessage.next = message;
            notifyAll();
        }
        return true;
    }

    public MyMessage removeMessage(MyHandler target, int token) {
        MyMessage preMessage = queueHeader;
        MyMessage removeResult = null;
        synchronized (this) {
            if (isQuit) {
                return null;
            }
            while (preMessage.next != null && preMessage.next.token != token && preMessage.next.target != target) {
                preMessage = preMessage.next;
            }
            if (preMessage.next != null) {
                removeResult = preMessage.next;
                preMessage.next = removeResult.next;
            }
        }
        return removeResult;
    }

    public MyMessage next() {
        MyMessage nextMessage = null;
        synchronized (this) {
            try {
                while (queueHeader.next == null) {
                    wait();
                    if (isQuit) {
                        return null;
                    }
                }
                MyMessage preMessage = queueHeader;
                nextMessage = queueHeader.next;
                while (nextMessage == null || System.currentTimeMillis() - nextMessage.handleTime < 0) {
                    wait();
                    if (isQuit) {
                        return null;
                    }
                    nextMessage = queueHeader.next; // 被唤醒需要获取新的Message
                    // 处理加急消息，保证例如刷新UI的消息先被执行,
                    if (nextMessage.target == null) {
                        do {
                            preMessage = nextMessage;
                            nextMessage = nextMessage.next;
                        } while (nextMessage != null && !nextMessage.isAsync());
                    }
                }
                preMessage.next = nextMessage.next;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return nextMessage;
    }

    public void quit() {
        synchronized (this) {
            isQuit = true;
            queueHeader.next = null;
            notifyAll();
        }
    }

    public int postBarrier() {
        return postBarrier(System.currentTimeMillis());
    }

    public int postBarrier(long when) {
        synchronized (this) {
            MyMessage preMessage = queueHeader;
            while (preMessage.next != null && preMessage.next.handleTime < when) {
                preMessage = preMessage.next;
            }
            MyMessage message = MyMessage.obtain();
            message.setAsync(true);
            message.token = mNextBarrierToken ++;
            message.next = preMessage.next;
            preMessage.next = message;
            return mNextBarrierToken;
        }
    }

    public void removeBarrier(int token) {
        synchronized (this) {
            MyMessage preMessage = queueHeader;
            while (preMessage.next != null && preMessage.next.token != token && preMessage.next.target != null) {
                preMessage = preMessage.next;
            }
            if (preMessage.next != null) {
                preMessage.next = preMessage.next.next;
            }
        }
    }
}
