package com.hc.my_views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * 自定义进度条
 */
public class ProgressView extends View {
    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint textPaint;
    private RectF oval;
    protected int mBackgroundColor = Color.BLUE;
    protected int mForegroundColor = Color.RED;
    protected CharSequence mProgressText;
    protected int mProgressTextColor = Color.RED;
    protected int mProgressTextSize = 10;
    protected int mEdgeWidth = 10;
    private boolean isLoaded = false;
    private int mProgress = 0;
    private int max = 4000;
    // 圆心位置
    private int x;
    private int y;
    // 圆半径
    private int r;
    private String mText;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
        init();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
        init();
    }

    private void init() {
        // 背景圆画笔
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);
        backgroundPaint.setColor(mBackgroundColor);
        // 只画圆弧
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(mEdgeWidth);

        // 前景圆画笔
        foregroundPaint = new Paint();
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setDither(true);
        foregroundPaint.setColor(mForegroundColor);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(mEdgeWidth);

        // 画文字
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(mProgressTextColor);
        textPaint.setTextSize(mProgressTextSize);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mBackgroundColor = typedArray.getColor(R.styleable.ProgressView_backgroundColor, mBackgroundColor);
        mForegroundColor = typedArray.getColor(R.styleable.ProgressView_foregroundColor, mForegroundColor);
        mProgressText = typedArray.getText(R.styleable.ProgressView_progressText);
        mProgressTextColor = typedArray.getColor(R.styleable.ProgressView_progressTextColor, mProgressTextColor);
        mProgressTextSize = sp2px(typedArray.getDimension(R.styleable.ProgressView_progressTextSize, mProgressTextSize));
        mEdgeWidth = dp2px(typedArray.getDimension(R.styleable.ProgressView_edgeWidth, mEdgeWidth));
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画背景圆
        canvas.drawCircle(x, y, r - (float)mEdgeWidth / 2, backgroundPaint);

        float progress = (float)mProgress / max;
        if (progress == 0) {
            return;
        }
        // 画前景圆弧
        // 第二个参数是起始弧度数，第三个参数是终点弧位置
        canvas.drawArc(oval, 0, progress * 360, false, foregroundPaint);

        // 画文字
        String text = TextUtils.isEmpty(mText)? (int)(progress * 100) + "%" : mText;
        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int baseLineX = (int) (x - textWidth / 2);
        int baseLineY = (int) (y - (fontMetrics.descent + fontMetrics.ascent)/2);
        canvas.drawText(text, baseLineX, baseLineY, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 只认为是正方形
        int r = Math.min(width, height);
        setMeasuredDimension(r, r);
        float edgeCut = (float)mEdgeWidth / 2;
        if (!isLoaded) {
            // 计算圆心的位置，测量的时候就计算，只计算一次
            this.x = getMeasuredWidth() / 2;
            this.y = getMeasuredHeight() / 2;
            this.r = r / 2;
            oval = new RectF(edgeCut, edgeCut, r - edgeCut, r - edgeCut);
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

    public synchronized void setText(String text) {
        this.mText = text;
        invalidate();
    }
}
