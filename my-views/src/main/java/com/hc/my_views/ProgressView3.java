package com.hc.my_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProgressView3 extends View {
    private int currentShapeIndex = 0;
    private final Paint paint = new Paint();
    private final Path path = new Path(); // 用于画三角形
    private final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    protected int drawInterval = 1000;
    private volatile boolean flag = true;

    public ProgressView3(Context context) {
        this(context, null);
    }

    public ProgressView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttr(context, attrs);
        init();
    }

    private void loadAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView3);
        drawInterval = typedArray.getInteger(R.styleable.ProgressView3_drawInterval, drawInterval);
        typedArray.recycle();
    }

    private void init() {
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int side = Math.min(width, height);
        setMeasuredDimension(side, side);
        // 等边三角形
        float triangleSide = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        float downDistance = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom() // 为了使三角形位于中间，需要下移
                - triangleSide/2 * (float)Math.sqrt(3)) / 2;
        float pointY = (float) (triangleSide/2 * Math.sqrt(3) + getPaddingTop()) + downDistance; // 三角形底部点的y坐标
        path.moveTo(triangleSide / 2f + getPaddingLeft(), getPaddingTop() + downDistance);
        path.lineTo(getPaddingLeft(), pointY);
        path.lineTo(getMeasuredWidth() - getPaddingRight(), pointY);
        path.close();
    }

    public void start() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                invalidate();
                if (flag) {
                    MAIN_HANDLER.postDelayed(this, drawInterval);
                }
            }
        };
        MAIN_HANDLER.postDelayed(r, drawInterval);
    }

    public void stop() {
        flag = false;
    }

    public void start(int time) {
        if (time < 0) {
            throw new IllegalArgumentException("time not be less than 0");
        }
        final Handler DRAWER_HANDLER = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (flag && msg.arg1 > 0) {
                    invalidate();
                    Message delayMsg = this.obtainMessage();
                    delayMsg.arg1 = msg.arg1 - drawInterval;
                    this.sendMessageDelayed(delayMsg, drawInterval);
                }
            }
        };
        Message msg = DRAWER_HANDLER.obtainMessage();
        msg.arg1 = time;
        DRAWER_HANDLER.sendMessageDelayed(msg, drawInterval);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (shapes[currentShapeIndex]) {
            case SQUARE:
                drawSquare(canvas);
                break;
            case CIRCLE:
                drawCircle(canvas);
                break;
            case TRIANGLE:
                drawTriangle(canvas);
                break;
        }
        currentShapeIndex = ++currentShapeIndex % shapes.length;
    }

    private final Shape [] shapes = { Shape.CIRCLE, Shape.SQUARE, Shape.TRIANGLE };
    private enum Shape {
        CIRCLE,
        SQUARE,
        TRIANGLE
    }

    private void drawCircle(Canvas canvas) {
        paint.setColor(Color.RED);
        // 计算圆心位置和半径
        int x = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
        int y = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop();
        float r = Math.min(x, y);
        canvas.drawCircle(x, y, r, paint);
    }

    private void drawSquare(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom(), paint);
    }


    private void drawTriangle(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        canvas.drawPath(path, paint);
    }
}
