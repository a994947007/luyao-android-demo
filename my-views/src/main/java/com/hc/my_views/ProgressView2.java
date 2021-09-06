package com.hc.my_views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 仿快手进度条，通过updateProgress更新进度
 */
public class ProgressView2 extends View {
    private Paint outGroundPaint;
    private Paint inGroundPaint;
    private RectF oval;
    protected int mProgressColor = Color.BLUE;
    protected int mSideWidth = 10;
    private boolean isLoaded = false;
    private int mProgress = 0;
    private int max = 4000;
    // 圆心位置
    private int x;
    private int y;
    // 圆半径
    private int r;
    private int cirClePadding = 10;

    public ProgressView2(Context context) {
        super(context);
        init();
    }

    public ProgressView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        init();
    }

    public ProgressView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
        init();
    }


    private void init() {
        // 背景圆画笔
        outGroundPaint = new Paint();
        outGroundPaint.setAntiAlias(true);
        outGroundPaint.setDither(true);
        outGroundPaint.setColor(mProgressColor);
        // 只画圆弧
        outGroundPaint.setStyle(Paint.Style.STROKE);
        outGroundPaint.setStrokeWidth(mSideWidth);

        // 前景圆画笔
        inGroundPaint = new Paint();
        inGroundPaint.setAntiAlias(true);
        inGroundPaint.setDither(true);
        inGroundPaint.setColor(mProgressColor);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView2);
        mProgressColor = typedArray.getColor(R.styleable.ProgressView2_progressColor, mProgressColor);
        mProgressColor = typedArray.getColor(R.styleable.ProgressView2_progressColor, mProgressColor);
        mSideWidth = dp2px(typedArray.getDimension(R.styleable.ProgressView2_sideWidth, mSideWidth));
        cirClePadding = dp2px(typedArray.getDimension(R.styleable.ProgressView2_cirClePadding, cirClePadding));
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画背景圆
        canvas.drawCircle(x, y, r - (float)mSideWidth / 2, outGroundPaint);
        float progress = (float)mProgress / max;
        if (progress == 0) {
            return;
        }
        // 画前景圆弧
        // 第二个参数是起始弧度数，第三个参数是终点弧位置
        canvas.drawArc(oval, -90, progress * 360, true, inGroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 只认为是正方形
        int r = Math.min(width, height);
        setMeasuredDimension(r, r);
        float edgeCut = (float)mSideWidth / 2;
        if (!isLoaded) {
            // 计算圆心的位置
            this.x = getMeasuredWidth() / 2;
            this.y = getMeasuredHeight() / 2;
            this.r = r / 2;
            oval = new RectF(edgeCut + cirClePadding , edgeCut + cirClePadding,
                    r - edgeCut - cirClePadding, r - edgeCut - cirClePadding);
        }
        isLoaded = true;
    }

    public synchronized void updateProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("The progress cannot be less than 0");
        }
        this.mProgress = progress;
        invalidate();
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The max cannot be less than 0");
        }
        this.max = max;
    }

    static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    static int sp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
