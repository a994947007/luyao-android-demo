package com.jny.common.download;

import android.graphics.Bitmap;

import com.hc.support.rxJava.observable.Observable;

import java.net.URL;

public interface DownloadService {

    /**
     * 小图片
     */
    Bitmap downloadImage(URL url);

    /**
     * 小图片
     */
    Observable<Bitmap> downloadImageObservable(URL url);

    void downloadFile(URL url);
}
