package com.hc.support.looper;

public class MyHandlerThread extends Thread{

    private MyLooper myLooper;

    @Override
    public void run() {
        MyLooper.prepare();
        synchronized (this) {
            myLooper = MyLooper.myLooper();
            notifyAll();
        }
        MyLooper.loop();
    }

    public MyLooper getLooper() {
        if (!isAlive()) {
            return null;
        }
        synchronized (this) {
            if (myLooper == null && isAlive()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return myLooper;
        }
    }
}
