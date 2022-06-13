package com.jny.download.remote;

import android.content.Intent;

public class DownloadCallbackIdCreator {
    private static volatile DownloadCallbackIdCreator mInstance = null;

    private int seq = 0;

    private DownloadCallbackIdCreator() { }

    static {
        if (mInstance == null) {
            synchronized (DownloadCallbackIdCreator.class) {
                if (mInstance == null) {
                    mInstance = new DownloadCallbackIdCreator();
                }
            }
        }
    }

    public static DownloadCallbackIdCreator getInstance() {
        return mInstance;
    }

    public String makeId() {
        seq = ++ seq % Integer.MAX_VALUE;
        return "" + System.currentTimeMillis() + seq;
    }
}
