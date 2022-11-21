package com.hc.android_demo.fragment.content.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.hc.android_demo.R;
import com.hc.android_demo.fragment.content.player.LuSurfaceTexturePlayer;
import com.hc.util.ViewUtils;

public class FloatingWindowService extends Service {

    public static final String VIDEO_PATH_KEY = "VIDEO_PATH_KEY";

    private WindowManager windowManager;

    private String videoPath;

    private View contentView;
    private TextureView textureView;
    private LuSurfaceTexturePlayer mPlayer;
    private WindowManager.LayoutParams layoutParams;
    private View closeView;

    private boolean isShow;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        videoPath = intent.getStringExtra(VIDEO_PATH_KEY);
        if (!isShow) {
            showFloatingWindow();
            isShow = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        // 移除浮动框
        if (windowManager != null) {
            windowManager.removeView(contentView);
        }
        mPlayer.stop();
        super.onDestroy();
    }

    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(FloatingWindowService.this)) {
                LayoutInflater inflater = LayoutInflater.from(FloatingWindowService.this);
                contentView = inflater.inflate(R.layout.floating_window_texture_view, null);
                textureView = contentView.findViewById(R.id.video_content);
                closeView = contentView.findViewById(R.id.close_btn);
                mPlayer = new LuSurfaceTexturePlayer(textureView, videoPath, 0);
                textureView.setSurfaceTextureListener(mPlayer);
                layoutParams = generateLayoutParams();
                windowManager.addView(contentView, layoutParams);
                contentView.setOnTouchListener(new FloatingOnTouchListener());
                closeView.setOnClickListener(v -> stopSelf());
                mPlayer.play();
            }
        }
    }

    private WindowManager.LayoutParams generateLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.RGB_565;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // android 8.0及以后使用
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // android 8.0以前使用
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.gravity = Gravity.START | Gravity.CENTER;
        //该flags描述的是窗口的模式，是否可以触摸，可以聚焦等
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置视频的播放窗口大小
        layoutParams.width = ViewUtils.getDisplayWidth(FloatingWindowService.this)/2;
        layoutParams.height = ViewUtils.getDisplayHeight(FloatingWindowService.this)/6;
        layoutParams.x = 700;
        layoutParams.y = 0;
        return layoutParams;
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {

        private int x;
        private int y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int)event.getRawX();
                    y = (int)event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(contentView, layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return false;
        }
    }
}
