package com.jny.common.download;

import android.graphics.Bitmap;

import androidx.lifecycle.LifecycleOwner;

import com.hc.support.rxJava.observable.Observable;
import com.jny.common.webview.DownloadCallback;

import java.net.URL;

public interface DownloadService {

    @interface Type {
        int UNKNOWN = 0;
        int IMAGE = 1;
        int VIDEO = 2;
        int AUDIO = 3;
    }

    /**
     * 小图片
     */
    Bitmap downloadImage(URL url);

    /**
     * 小图片
     */
    Observable<Bitmap> downloadImageObservable(URL url);

    void downloadFile(String url, @Type int type, LifecycleOwner lifecycleOwner, DownloadCallback downloadCallback);
}
