package com.jny.download.remote;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface DownloadCallbackProcessInterface extends IInterface {

    String DESCRIPTOR = "com.jny.download.remote.DownloadCallbackProcessInterface";
    int ERROR_CODE = IBinder.FIRST_CALL_TRANSACTION;
    int SUCCESS_CODE = IBinder.FIRST_CALL_TRANSACTION + 1;

    void onError(String id, String err) throws RemoteException;

    void onSuccess(String id) throws RemoteException;
}
