package com.jny.download.bitmap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.hc.util.UriUtils;
import com.jny.common.download.DownloadService;
import com.jny.download.remote.DownloadCallbackDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class FileDownloadService extends JobIntentService {

    private static final int JOB_ID = 1000;
    public static final String URL_KEY = "URK_KEY";
    public static final String TYPE_KEY = "TYPE_KEY";
    public static final String CALLBACK_ID_KEY = "CALLBACK_ID_KEY";

    public static void enQueueWork(Context context, Intent work) {
        enqueueWork(context, FileDownloadService.class, JOB_ID, work);
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        DownloadCallbackDispatcher.getInstance().initConnection();
        return super.onBind(intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String urlString = intent.getStringExtra(URL_KEY);
        String id = intent.getStringExtra(CALLBACK_ID_KEY);
        @DownloadService.Type
        int type = intent.getIntExtra(TYPE_KEY, DownloadService.Type.UNKNOWN);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(urlString);
            inputStream = url.openStream();
            Uri uri = getUri(type, makeFileName(type));
            outputStream = getContentResolver().openOutputStream(uri);
            byte[] data = new byte[1024];
            int len;
            while ((len = inputStream.read(data)) > 0) {
                outputStream.write(data, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            DownloadCallbackDispatcher.getInstance().onSuccess(id);
        } catch (IOException e) {
            e.printStackTrace();
            DownloadCallbackDispatcher.getInstance().onError(id, e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String makeFileName(@DownloadService.Type int type) {
        String suffix = "";
        switch (type) {
            case DownloadService.Type.IMAGE:
                suffix = ".jpg";
                break;
            case DownloadService.Type.VIDEO:
                suffix = ".mp4";
                break;
            case DownloadService.Type.AUDIO:
                suffix = ".mp3";
                break;
        }
        return System.currentTimeMillis() + suffix;
    }

    private static Uri getUri(@DownloadService.Type int type, String filename) {
        switch (type) {
            case DownloadService.Type.IMAGE:
                return UriUtils.getImageUri(filename);
            case DownloadService.Type.VIDEO:
                return UriUtils.getVideoUri(filename);
            case DownloadService.Type.AUDIO:
                return UriUtils.getAudioUri(filename);
        }
        return null;
    }


}
