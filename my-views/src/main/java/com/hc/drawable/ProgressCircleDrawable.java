package com.hc.drawable;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProgressCircleDrawable extends Drawable {

    private Paint mPaint;
    private int mStartColor;
    private int mEndColor;
    private int mStrokeWidth;
    private float mStartAngle;

    private RectF mOval;
    private boolean mIsInitDraw;
    private SweepGradient mSweepGradient;

    public ProgressCircleDrawable() {
        init();
    }

    private void init() {
        this.mPaint = new Paint();
        mStrokeWidth = 10;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mStrokeWidth);

        mStartColor = Color.TRANSPARENT;
        mEndColor = Color.parseColor("#222222");
        mStartAngle = 0f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);         // 重新开始
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);       // 重复的模式，重新开始
        valueAnimator.setInterpolator(new LinearInterpolator());    // 插值器
        valueAnimator.addUpdateListener(animation -> {
            mStartAngle = (float) animation.getAnimatedValue();
            invalidateSelf();
        });
        valueAnimator.start();
    }

    private void initDraw() {
        if (mIsInitDraw) {
            return;
        }
        mIsInitDraw = true;
        mOval = new RectF(mStrokeWidth / 2f,
                mStrokeWidth / 2f,
                getBounds().width() - mStrokeWidth / 2f,
                getBounds().height() - mStrokeWidth / 2f);
        int [] colors = {mStartColor, mEndColor};
        float [] positions = {0, 0.75f};
        mSweepGradient = new SweepGradient(
                mOval.centerX(),
                mOval.centerY(),
                colors,
                positions
        );
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        initDraw();
        canvas.rotate(mStartAngle * 360, mOval.centerX(), mOval.centerY());
        mPaint.setShader(mSweepGradient);
        canvas.drawArc(mOval,
                0,
                270,
                false,
                mPaint);
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
