package com.jny.process.main;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jny.process.DownloadCallbackProgressInterface;

public abstract class DownloadCallbackServer extends Binder implements DownloadCallbackProgressInterface {
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case DOWNLOAD_ON_START_CODE:
                data.enforceInterface(DESCRIPTOR);
                String id = data.readString();
                this.onStart(id);
                reply.writeNoException();
                return true;
            case DOWNLOAD_ON_DOWNLOAD_CODE:
                data.enforceInterface(DESCRIPTOR);
                id = data.readString();
                float progress = data.readFloat();
                this.onDownload(id, progress);
                reply.writeNoException();
                return true;
            case DOWNLOAD_ON_SUCCESS_CODE:
                data.enforceInterface(DESCRIPTOR);
                id = data.readString();
                String resultPath = data.readString();
                this.onSuccess(id, resultPath);
                reply.writeNoException();
                return true;
            case DOWNLOAD_ON_ERROR_CODE:
                data.enforceInterface(DESCRIPTOR);
                id = data.readString();
                Throwable r = (Throwable) data.readSerializable();
                this.onError(id, r);
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
