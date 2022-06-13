package com.hc.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SwipeShadow extends Drawable {

    private Paint mPaint;
    private Path mPath;

    private int mStartColor;
    private int mEndColor;

    private boolean isInit = false;

    public SwipeShadow() {
        init();
    }

    private void init() {
        mStartColor = Color.parseColor("#33FFFFFF");
        mEndColor = Color.parseColor("#00FFFFFF");
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPath = new Path();
    }

    private void initShadow(int width, int height) {
        if (isInit) {
            return;
        }
        isInit = true;
        mPaint.setShader(new LinearGradient(0, 0, width, 0, mStartColor, mEndColor, Shader.TileMode.CLAMP));
        mPath.reset();
        mPath.moveTo(20, 0);
        mPath.quadTo(-40, height / 2f, 20,height);
        mPath.lineTo(width, height);
        mPath.lineTo(width, 0);
        mPath.close();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        initShadow(getBounds().width(), getBounds().height());
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
