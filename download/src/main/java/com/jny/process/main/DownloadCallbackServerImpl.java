package com.jny.process.main;

import com.jny.core.DownloadCallback;

public class DownloadCallbackServerImpl extends DownloadCallbackServer{

    private static volatile DownloadCallbackServerImpl mInstance = null;

    static {
        if (mInstance == null) {
            synchronized (DownloadCallbackServerImpl.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCallbackServerImpl();
                }
            }
        }
    }

    private DownloadCallbackServerImpl(){ }

    public static DownloadCallbackServerImpl getInstance() {
        return mInstance;
    }

    @Override
    public void onStart(String id) {
        DownloadCallback downloadCallback = DownloadCallbackManager.getInstance().getDownloadCallback(id);
        if (downloadCallback != null) {
            downloadCallback.onStart();
        }
    }

    @Override
    public void onDownload(String id, float progress) {
        DownloadCallback downloadCallback = DownloadCallbackManager.getInstance().getDownloadCallback(id);
        if (downloadCallback != null) {
            downloadCallback.onDownload(progress);
        }
    }

    @Override
    public void onSuccess(String id, String resultPath) {
        DownloadCallback downloadCallback = DownloadCallbackManager.getInstance().getDownloadCallback(id);
        if (downloadCallback != null) {
            downloadCallback.onSuccess(resultPath);
        }
    }

    @Override
    public void onError(String id, Throwable r) {
        DownloadCallback downloadCallback = DownloadCallbackManager.getInstance().getDownloadCallback(id);
        if (downloadCallback != null) {
            downloadCallback.onError(r);
        }
    }
}
