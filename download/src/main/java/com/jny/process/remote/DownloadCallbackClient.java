package com.jny.process.remote;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.jny.process.DownloadCallbackProgressInterface;

public class DownloadCallbackClient implements DownloadCallbackProgressInterface {

    private final IBinder mRemote;

    public DownloadCallbackClient(IBinder remote) {
        this.mRemote = remote;
    }

    @Override
    public void onStart(String id) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(id);
            mRemote.transact(DOWNLOAD_ON_START_CODE, data, reply, 0);
            reply.readException();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public void onDownload(String id, float progress) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(id);
            data.writeFloat(progress);
            mRemote.transact(DOWNLOAD_ON_DOWNLOAD_CODE, data, reply, 0);
            reply.readException();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public void onSuccess(String id, String resultPath) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(id);
            data.writeString(resultPath);
            mRemote.transact(DOWNLOAD_ON_SUCCESS_CODE, data, reply, 0);
            reply.readException();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public void onError(String id, Throwable r) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(id);
            data.writeSerializable(r);
            mRemote.transact(DOWNLOAD_ON_ERROR_CODE, data, reply, 0);
            reply.readException();
        } catch (RemoteException e) {
            e.printStackTrace();
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
