package com.hc.support.rxJava.schedule;

import com.hc.support.rxJava.disposable.Disposable;

import java.util.concurrent.TimeUnit;

public abstract class Scheduler {
    public abstract Worker createWorker();

    public abstract static class Worker implements Disposable {
        public abstract Disposable schedule(Runnable run, long delay, TimeUnit unit);
    }

    public Disposable schedule(final Runnable run, long delay, TimeUnit unit) {
        Worker worker = createWorker();
        return worker.schedule(run, delay, unit);
    }

    public Disposable scheduleDirect(final Runnable run) {
        Worker worker = createWorker();
        return worker.schedule(run, 0,  TimeUnit.NANOSECONDS);
    }
}
