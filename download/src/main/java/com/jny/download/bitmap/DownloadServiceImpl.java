package com.jny.download.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.auto.service.AutoService;
import com.hc.support.rxJava.observable.Observable;
import com.hc.support.rxJava.schedule.Schedules;
import com.jny.common.download.DownloadService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@AutoService({DownloadService.class})
public class DownloadServiceImpl implements DownloadService {
    @Override
    public Bitmap downloadImage(URL url) {
        try {
            InputStream inputStream = url.openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Observable<Bitmap> downloadImageObservable(URL url) {
        return Observable.just(url)
                .map(URL::openStream)
                .map(BitmapFactory::decodeStream)
                .subscribeOn(Schedules.IO);
    }

    @Override
    public void downloadFile(URL url) {

    }
}
