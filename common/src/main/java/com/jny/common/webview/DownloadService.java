package com.jny.common.webview;

import android.graphics.Bitmap;

import java.net.URL;

public interface DownloadService {
    Bitmap downloadImage(URL url);

    void downloadFile(URL url);
}
