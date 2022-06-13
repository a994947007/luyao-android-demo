package com.jny.download.remote;

import android.os.RemoteException;
import android.widget.Toast;

import com.hc.util.ToastUtils;
import com.jny.common.webview.DownloadCallback;

import java.util.HashMap;
import java.util.Map;

public class DownloadCallbackServerImpl extends DownloadCallbackServer {

    private static volatile DownloadCallbackServerImpl mInstance = null;

    private final Map<String, DownloadCallback> mDownloadCallbackMap = new HashMap<>();

    private DownloadCallbackServerImpl() { }

    static {
        if (mInstance == null) {
            synchronized (DownloadCallbackProcessInterface.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCallbackServerImpl();
                }
            }
        }
    }

    public void registerDownloadCallback(String id, DownloadCallback downloadCallback) {
        mDownloadCallbackMap.put(id, downloadCallback);
    }

    public void unregisterDownloadCallback(String id) {
        mDownloadCallbackMap.remove(id);
    }

    public static DownloadCallbackServerImpl getInstance() {
        return mInstance;
    }

    @Override
    public void onError(String id, String err) throws RemoteException {
        DownloadCallback downloadCallback = mDownloadCallbackMap.get(id);
        if (downloadCallback != null) {
            downloadCallback.onError(err);
        }
    }

    @Override
    public void onSuccess(String id) throws RemoteException {
        DownloadCallback downloadCallback = mDownloadCallbackMap.get(id);
        if (downloadCallback != null) {
            downloadCallback.onSuccess();
        }
    }
}
