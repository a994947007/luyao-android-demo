package com.hc.my_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PullAnimatedView extends ConstraintLayout {

    private Path mPath;
    private Paint mPaint;
    private int OTHER_ALPHA = 110;

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int controlX;
    private int controlY;
    private Rect mRect;

    public PullAnimatedView(@NonNull Context context) {
        super(context);
    }

    public PullAnimatedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullAnimatedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setARGB(255, 244, 92, 71);
        mRect = new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawARGB(0, 0, 0, 0);
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(startX, startY);
        mPath.quadTo(controlX, controlY, endX, endY);
        mPath.lineTo(getMeasuredWidth(), 0);
        mPath.close();
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        super.onDraw(canvas);
    }

    public void show(int startX, int startY, int endX, int endY, int controlX, int controlY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.controlX = controlX;
        this.controlY = controlY;
        invalidate();
    }
}
