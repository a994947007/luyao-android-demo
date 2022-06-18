package com.jny.process;

import android.os.IBinder;
import android.os.IInterface;

public interface DownloadCallbackProgressInterface extends IInterface {

    String DESCRIPTOR = "com.jny.process.remote.DownloadCallbackProcessInterface";
    int DOWNLOAD_ON_START_CODE = IBinder.FIRST_CALL_TRANSACTION;
    int DOWNLOAD_ON_DOWNLOAD_CODE = IBinder.FIRST_CALL_TRANSACTION + 1;
    int DOWNLOAD_ON_SUCCESS_CODE = IBinder.FIRST_CALL_TRANSACTION + 2;
    int DOWNLOAD_ON_ERROR_CODE = IBinder.FIRST_CALL_TRANSACTION + 3;

    void onStart(String id);

    void onDownload(String id, float progress);

    void onSuccess(String id, String resultPath);

    void onError(String id, Throwable r);
}
