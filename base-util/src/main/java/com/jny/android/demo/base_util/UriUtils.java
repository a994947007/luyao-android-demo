package com.jny.android.demo.base_util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public class UriUtils {

    public static String getUri(int resId) {
        Resources r = AppEnvironment.getAppContext().getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));
        return uri.toString();
    }

    public static Uri getImageUri(String filename) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, filename);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
        } else {
            String dstPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + Environment.DIRECTORY_PICTURES
                    + File.separator
                    + filename;
            contentValues.put(MediaStore.Images.ImageColumns.DATA, dstPath);
        }
        return AppEnvironment.getAppContext()
                .getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static Uri getAudioUri(String filename) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Audio.AudioColumns.DISPLAY_NAME, filename);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Audio.AudioColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC);
        } else {
            String dstPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + Environment.DIRECTORY_MUSIC
                    + File.separator
                    + filename;
            contentValues.put(MediaStore.Audio.AudioColumns.DATA, dstPath);
        }
        return AppEnvironment.getAppContext()
                .getContentResolver()
                .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static Uri getVideoUri(String filename) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.VideoColumns.DISPLAY_NAME, filename);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Video.VideoColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
        } else {
            String dstPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + Environment.DIRECTORY_MOVIES
                    + File.separator
                    + filename;
            contentValues.put(MediaStore.Video.VideoColumns.DATA, dstPath);
        }
        return AppEnvironment.getAppContext()
                .getContentResolver()
                .insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static Uri getDownloadsUri(String filename) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, filename);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            return AppEnvironment.getAppContext()
                    .getContentResolver()
                    .insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            String dstPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + Environment.DIRECTORY_DOWNLOADS
                    + File.separator
                    + filename;
            contentValues.put(MediaStore.Downloads.DATA, dstPath);
            return AppEnvironment.getAppContext()
                    .getContentResolver()
                    .insert(Uri.parse(dstPath), contentValues);
        }
    }
}
