package com.hc.android_demo.fragment.content.player;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;

import java.io.IOException;

public class LuSurfaceTexturePlayer implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnVideoSizeChangedListener {

    private SurfaceTexture mSurfaceTexture;
    private MediaPlayer mMediaPlayer;
    private final String mUrl;
    private final int mInitVolume;
    private TextureView mTextureView;

    public LuSurfaceTexturePlayer(TextureView textureView, String url, int initVolume) {
        mUrl = url;
        mInitVolume = initVolume;
        mTextureView = textureView;
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        try{
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setLooping(true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.setVolume(mInitVolume, mInitVolume);
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface;
        initMediaPlayer();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) { }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        stop();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp !=null && mp.getVideoWidth() != 0 && mp.getVideoHeight() != 0) {
            play();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        int textureWidth = mTextureView.getMeasuredWidth();
        mTextureView.getLayoutParams().width = textureWidth;
        mTextureView.getLayoutParams().height = textureWidth * height / width;
        mTextureView.requestLayout();
    }
}
