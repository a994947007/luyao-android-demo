package com.jny.download.remote;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class DownloadCallbackClient implements DownloadCallbackProcessInterface {

    private final IBinder mRemote;

    public DownloadCallbackClient(IBinder remote) {
        mRemote = remote;
    }

    @Override
    public void onError(String id, String err) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(id);
            data.writeString(err);
            mRemote.transact(ERROR_CODE, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public void onSuccess(String id) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(id);
            mRemote.transact(ERROR_CODE, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return mRemote;
    }
}
