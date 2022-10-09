package com.jny.download;

import android.graphics.Bitmap;

import androidx.lifecycle.Lifecycle;

import com.jny.android.demo.rxandroid.observable.Observable;

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

    /**
     * 文件下载，跨进程
     */
    void downloadFile(Lifecycle lifecycle, String url, DownloadCallback downloadCallback);

    /**
     * 文件下载，跨进程
     */
    Observable<String> observeDownloadFile(Lifecycle lifecycle, String url);
}
