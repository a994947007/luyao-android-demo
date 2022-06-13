package com.jny.download.remote;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class DownloadCallbackServer extends Binder implements DownloadCallbackProcessInterface {

    public DownloadCallbackServer() {
        this.attachInterface(this, DESCRIPTOR);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
            {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case ERROR_CODE:
                data.enforceInterface(DESCRIPTOR);
                String id = data.readString();
                String error = data.readString();
                this.onError(id, error);
                reply.writeNoException();
                return true;
            case SUCCESS_CODE:
                data.enforceInterface(DESCRIPTOR);
                this.onSuccess(data.readString());
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}
