package com.hc.android_demo.fragment.content.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.io.IOException;

public class LuPlayer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback {

    private MediaPlayer mediaPlayer;

    public String mUrl;

    public LuPlayer(String url) {
        this.mUrl = url;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp.getVideoWidth() != 0 && mp.getVideoHeight() != 0) {
            mp.start();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        holder.addCallback(this);
        this.mediaPlayer = new MediaPlayer();
        try {
            this.mediaPlayer.setDataSource(mUrl);
            this.mediaPlayer.setDisplay(holder);
            this.mediaPlayer.prepareAsync();
            this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mediaPlayer.setOnBufferingUpdateListener(this);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        stop();
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
