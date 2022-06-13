package com.jny.download.remote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.hc.base.AppEnvironment;

public class DownloadCallbackDispatcher {
    private static volatile DownloadCallbackDispatcher mInstance = null;

    private final MainProgressConnection mMainProgressConnection = new MainProgressConnection();

    private DownloadCallbackDispatcher() { }

    static {
        if (mInstance == null) {
            synchronized (DownloadCallbackDispatcher.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCallbackDispatcher();
                }
            }
        }
    }

    public static DownloadCallbackDispatcher getInstance() {
        return mInstance;
    }

    public void initConnection() {
        Intent intent = new Intent(AppEnvironment.getAppContext(), DownloadService.class);
        AppEnvironment.getAppContext().bindService(intent, mMainProgressConnection, Context.BIND_AUTO_CREATE);
    }

    public void onError(String id, String err) {
        mMainProgressConnection.onError(id, err);
    }

    public void onSuccess(String id) {
        mMainProgressConnection.onSuccess(id);
    }

    private static class MainProgressConnection implements ServiceConnection {
        private DownloadCallbackProcessInterface mMainProgressCallback;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMainProgressCallback = new DownloadCallbackClient(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMainProgressCallback = null;
        }

        public void onError(String id, String err) {
            try {
                mMainProgressCallback.onError(id, err);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onSuccess(String id) {
            try {
                mMainProgressCallback.onSuccess(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
