package com.hc.design.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.design.R;

public class CircleBitmapDrawable extends Drawable {
    private final Paint mPaint;
    private final Bitmap mBitmap;
    private final Matrix mMatrix;
    private final BitmapShader mBitmapShader;
    private RectF mRectF = null;
    private Rect mTempRect = new Rect();

    private int mRadius = 0;
    boolean mUseTopLeftCircle;
    boolean mUseTopRightCircle;
    boolean mUseBottomLeftCircle;
    boolean mUseBottomRightCircle;

    public CircleBitmapDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mMatrix = new Matrix();
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    public CircleBitmapDrawable(Bitmap bitmap,
                                int radius) {
        this(bitmap);
        mRadius = radius;
    }

    public CircleBitmapDrawable(Bitmap bitmap,
                                int radius,
                                boolean useTopLeftCircle,
                                boolean useTopRightCircle,
                                boolean useBottomLeftCircle,
                                boolean useBottomRightCircle) {
        this(bitmap, radius);
        mUseTopLeftCircle = useTopLeftCircle;
        mUseTopRightCircle = useTopRightCircle;
        mUseBottomLeftCircle = useBottomLeftCircle;
        mUseBottomRightCircle = useBottomRightCircle;
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mRectF == null) {
            mRectF =  new RectF(0, 0, getBounds().width(), getBounds().height());
        }
        int srcW = mBitmap.getWidth();
        int srcH = mBitmap.getHeight();
        float scaleW = ((float) getBounds().width()) / srcW;
        float scaleH = ((float) getBounds().height()) / srcH;
        mMatrix.postScale(scaleW, scaleH);
        mBitmapShader.setLocalMatrix(mMatrix); //将矩阵变化设置到BitmapShader,其实就是作用到Bitmap
        mPaint.setShader(mBitmapShader);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
        if (!mUseTopLeftCircle) {
            resetTempRect(0, 0, mRadius, mRadius);
            canvas.drawRect(mTempRect, mPaint);
        }
        if (!mUseTopLeftCircle) {
            resetTempRect(getBounds().width() - mRadius, 0, mRadius, mRadius);
            canvas.drawRect(mTempRect, mPaint);
        }
        if (!mUseBottomLeftCircle) {
            resetTempRect(0, getBounds().height() - mRadius, mRadius, mRadius);
            canvas.drawRect(mTempRect, mPaint);
        }
        if (!mUseBottomRightCircle) {
            resetTempRect(getBounds().width() - mRadius, getBounds().height() - mRadius, mRadius, mRadius);
            canvas.drawRect(mTempRect, mPaint);
        }
    }

    private void resetTempRect(int left, int top, int width, int height) {
        mTempRect.left = left;
        mTempRect.right = left + width;
        mTempRect.top = top;
        mTempRect.bottom = top + height;
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
