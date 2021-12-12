package com.hc.support.rxJava.schedule;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hc.support.rxJava.disposable.Disposable;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainScheduler extends Scheduler{
    @Override
    public Worker createWorker() {
        return new MainWorker();
    }

    private static class MainWorker extends Scheduler.Worker {

        private final MainWorkerHandler MAIN_HANDLER = new MainWorkerHandler(Looper.getMainLooper());
        private boolean isDisposed = false;
        private final int TOKEN = 0;

        @Override
        public void dispose() {
            if (!isDisposed) {
                isDisposed = true;
                MAIN_HANDLER.removeMessages(TOKEN);
            }
        }

        @Override
        public boolean isDisposable() {
            return isDisposed;
        }

        @Override
        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            if (delay <= 0) {
                MAIN_HANDLER.post(run);
            } else {
                MAIN_HANDLER.sendMessageDelayed(MAIN_HANDLER.obtain(TOKEN, run), TimeUnit.MILLISECONDS.convert(delay, unit));
            }
            return this;
        }

        private static class MainWorkerHandler extends Handler {
            private WeakReference<Runnable> runnable;

            public MainWorkerHandler(Looper looper) {
                super(looper);
            }

            public Message obtain(int token, Runnable runnable) {
                this.runnable = new WeakReference<>(runnable);
                return Message.obtain(this, token);
            }

            @Override
            public void handleMessage(@NonNull Message msg) {
                Runnable r = runnable.get();
                if (r != null) {
                    r.run();
                }
            }
        }
    }
}
