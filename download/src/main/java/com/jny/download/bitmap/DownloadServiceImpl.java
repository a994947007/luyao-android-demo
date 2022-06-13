package com.jny.download.bitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.auto.service.AutoService;
import com.hc.base.AppEnvironment;
import com.hc.support.rxJava.function.Function;
import com.hc.support.rxJava.observable.Observable;
import com.hc.support.rxJava.schedule.Schedules;
import com.jny.common.download.DownloadService;
import com.jny.common.webview.DownloadCallback;
import com.jny.download.remote.DownloadCallbackIdCreator;
import com.jny.download.remote.DownloadCallbackServerImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@AutoService({DownloadService.class})
public class DownloadServiceImpl implements DownloadService {
    @Override
    public Bitmap downloadImage(URL url) {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Observable<Bitmap> downloadImageObservable(URL url) {
        return Observable.just(url)
                .map(URL::openStream)
                .map(inputStream -> {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bitmap;
                })
                .subscribeOn(Schedules.IO);
    }

    @Override
    public void downloadFile(String url, @Type int type, LifecycleOwner lifecycleOwner, DownloadCallback downloadCallback) {
        Intent intent = new Intent(AppEnvironment.getAppContext(), FileDownloadService.class);
        intent.putExtra(FileDownloadService.URL_KEY, url);
        intent.putExtra(FileDownloadService.TYPE_KEY, type);
        downloadCallback.onStart();
        String id = DownloadCallbackIdCreator.getInstance().makeId();
        DownloadCallbackServerImpl.getInstance().registerDownloadCallback(id, downloadCallback);
        lifecycleOwner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event == Lifecycle.Event.ON_DESTROY) {
                DownloadCallbackServerImpl.getInstance().unregisterDownloadCallback(id);
            }
        });
        FileDownloadService.enQueueWork(AppEnvironment.getAppContext(), intent);
    }
}
