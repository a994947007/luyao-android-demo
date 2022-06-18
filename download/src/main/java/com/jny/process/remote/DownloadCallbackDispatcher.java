package com.jny.process.remote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hc.base.AppEnvironment;
import com.jny.process.DownloadCallbackProgressInterface;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DownloadCallbackDispatcher {

    private boolean isInit = false;
    private final Lock mLock = new ReentrantLock();
    private final Condition mCondition = mLock.newCondition();
    private static volatile DownloadCallbackDispatcher mInstance = null;
    private final MainProgressConnection mMainProgressConnection;

    static {
        if (mInstance == null) {
            synchronized (DownloadCallbackDispatcher.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCallbackDispatcher();
                }
            }
        }
    }

    private DownloadCallbackDispatcher() {
        mMainProgressConnection = new MainProgressConnection(mLock, mCondition);
    }

    public static DownloadCallbackDispatcher getInstance() {
        return mInstance;
    }

    public void initConnection() {
        Intent intent = new Intent(AppEnvironment.getAppContext(), DownloadMainProgressService.class);
        AppEnvironment.getAppContext().bindService(intent, mMainProgressConnection, Context.BIND_AUTO_CREATE);
        if (mMainProgressConnection.isConnected()) {
            return;
        }
        try {
            mLock.lock();
            while (!mMainProgressConnection.isConnected()) {
                mCondition.await();
            }
            isInit = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

    public boolean isInit() {
        return isInit;
    }

    public void onStart(String id) {
        mMainProgressConnection.onStart(id);
    }

    public void onDownload(String id, float progress) {
        mMainProgressConnection.onDownload(id, progress);
    }

    public void onSuccess(String id, String resultPath) {
        mMainProgressConnection.onSuccess(id, resultPath);
    }

    public void onError(String id, Throwable r) {
        mMainProgressConnection.onError(id, r);
    }

    private static class MainProgressConnection implements ServiceConnection {

        private DownloadCallbackProgressInterface mMainProgressCallback = null;

        private final Lock mLock;
        private final Condition mCondition;

        public MainProgressConnection(Lock lock, Condition condition) {
            this.mLock = lock;
            this.mCondition = condition;
        }

        public boolean isConnected() {
            return mMainProgressCallback != null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMainProgressCallback = new DownloadCallbackClient(service);
            if (DownloadCallbackDispatcher.getInstance().isInit()) {
                return;
            }
            try {
                mLock.lock();
                mCondition.signalAll();
            } finally {
                mLock.unlock();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMainProgressCallback = null;
        }

        public void onStart(String id) {
            mMainProgressCallback.onStart(id);
        }

        public void onDownload(String id, float progress) {
            mMainProgressCallback.onDownload(id, progress);
        }

        public void onSuccess(String id, String resultPath) {
            mMainProgressCallback.onSuccess(id, resultPath);
        }

        public void onError(String id, Throwable r) {
            mMainProgressCallback.onError(id, r);
        }
    }
}
