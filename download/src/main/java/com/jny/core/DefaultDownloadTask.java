package com.jny.core;

import android.content.Context;
import android.net.Uri;

import com.jny.android.demo.base_util.UriUtils;
import com.jny.util.DownloadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DefaultDownloadTask implements DownloadTask{
    private final Context mContext;
    private final DownloadCallback mCallback;
    private final String mDownloadUrl;

    public DefaultDownloadTask(Context context, String url, DownloadCallback callback) {
        this.mContext = context;
        this.mDownloadUrl = url;
        this.mCallback = callback;
    }

    private String makeFileName(String url) {
        return System.currentTimeMillis() + DownloadUtils.parseUrlSuffixes(url);
    }

    @DownloadType
    private int getFileType(String fileName) {
        String suffix = fileName.substring(fileName.indexOf('.'));
        int downloadType = DownloadType.UNKNOWN;
        switch (suffix) {
            case ".jpg":
            case ".png":
            case ".jpeg":
                downloadType = DownloadType.IMAGE;
                break;
            case ".mp4":
                downloadType = DownloadType.VIDEO;
                break;
            case ".mp3":
                downloadType = DownloadType.AUDIO;
                break;
        }
        return downloadType;
    }

    private static Uri getUri(@DownloadType int type, String filename) {
        switch (type) {
            case DownloadType.IMAGE:
                return UriUtils.getImageUri(filename);
            case DownloadType.VIDEO:
                return UriUtils.getVideoUri(filename);
            case DownloadType.AUDIO:
                return UriUtils.getAudioUri(filename);
            case DownloadType.DOC:
            case DownloadType.RAR:
            case DownloadType.UNKNOWN:
            case DownloadType.ZIP:
                return UriUtils.getDownloadsUri(filename);
        }
        return null;
    }

    @Override
    public void download() {
        mCallback.onStart();
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(mDownloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            int totalDownloadLength = conn.getContentLength();
            inputStream = conn.getInputStream();
            String fileName = makeFileName(mDownloadUrl);
            Uri uri = getUri(getFileType(fileName), fileName);
            outputStream = mContext.getContentResolver().openOutputStream(uri);
            byte[] data = new byte[1024];
            int len;
            int currentDownloadLength = 0;
            while ((len = inputStream.read(data)) > 0) {
                currentDownloadLength += len;
                outputStream.write(data, 0, len);
                mCallback.onDownload((float) currentDownloadLength / totalDownloadLength);
            }
            outputStream.flush();
            outputStream.close();
            mCallback.onSuccess(DownloadUtils.getPath(mContext, uri));
        } catch (IOException e) {
            mCallback.onError(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
