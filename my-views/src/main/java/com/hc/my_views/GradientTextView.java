package com.hc.my_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 自定义TextView，对应的属性定义在values/attrs.xml中，支持颜色渐变
 */
public class GradientTextView extends View {
    protected String mText;
    protected float mTextSize;
    protected int mTextColor;
    protected int mGravity;
    protected int mShadowColor;
    // 渐变进度
    private float leftPercent = 0.0f;
    private float rightPercent = 1.0f;
    private float topPercent = 0.0f;
    private float bottomPercent = 1.0f;

    // 用于测量文字
    public GradientTextView(@NonNull Context context) {
        super(context);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        this.mText = typedArray.getString(R.styleable.GradientTextView_android_text);
        this.mTextSize = typedArray.getDimension(R.styleable.GradientTextView_android_textSize, 80);
        this.mTextColor = typedArray.getColor(R.styleable.GradientTextView_android_textColor, Color.BLACK);
        this.mGravity = typedArray.getInt(R.styleable.GradientTextView_gravity, CENTER);
        typedArray.recycle();

        paint.setTextSize(mTextSize);  // 像素
        paint.setColor(mTextColor);
        paint.setAntiAlias(true);
    }

    private final Paint paint = new Paint();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int textWidth = (int) paint.measureText(mText);
        int textHeight = (int) (fontMetrics.bottom - fontMetrics.top);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measuredWidth = widthMode == MeasureSpec.EXACTLY ? MeasureSpec.getSize(widthMeasureSpec) : textWidth;
        int measuredHeight = heightMode == MeasureSpec.EXACTLY? MeasureSpec.getSize(heightMeasureSpec) : textHeight;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 文字位置
        // 将文字画在ViewGroup中间
        float textWidth = paint.measureText(mText);
        float xBaseline = (getWidth() >> 1) -  textWidth/ 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float yBaseline = (getHeight() >> 1) - (fontMetrics.ascent + fontMetrics.descent)/2;
        switch (mGravity) {
            case CENTER:
                break;      // 上述初始值默认为center
            case LEFT:
                xBaseline = 0;
                break;
            case TOP:
                yBaseline = 0;
                break;
            case RIGHT:
                xBaseline = getWidth();
                break;
            case BOTTOM:
                yBaseline = getHeight();
                break;
            case LEFT|TOP:
                xBaseline = 0;
                yBaseline = 0;
                break;
            case TOP|RIGHT:
                xBaseline = getWidth();
                yBaseline = 0;
                break;
            case RIGHT|BOTTOM:
                xBaseline = getWidth();
                yBaseline = getHeight();
                break;
            case BOTTOM|LEFT:
                xBaseline = 0;
                yBaseline = getHeight();
                break;
        }

        /*
        画渐变文字类似下面这个框，内部是渐变的部分，周围的部分是默认的颜色
        -----------------
        |   ---------   |
        |   |       |   |
        |   |       |   |
        |   ---------   |
        |               |
        -----------------
        其他部分需要分层四个矩形来画，这里不能先画整体，再画中间，这样使得中间重叠了，画布重叠容易造成卡顿
        画渐变文字类似下面这个框，内部是渐变的部分，周围的部分是默认的颜色
        -----------------
        |   |-------|   |
        |   |       |   |
        |   |       |   |
        |   |-------|   |
        |   |       |   |
        -----------------
        */

        // 从左往右计算，渐变效果
        float left = xBaseline + textWidth * leftPercent;
        float right = xBaseline + textWidth * rightPercent;
        // 从上往下计算，渐变效果
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        float top = yBaseline - (textHeight - fontMetrics.bottom - textHeight * topPercent);
        float bottom = yBaseline - (textHeight - fontMetrics.bottom - textHeight * bottomPercent);
        // 最外边的距离
        float lLeft = xBaseline;
        float tTop = yBaseline + fontMetrics.top;
        float rRight = xBaseline + textWidth;
        float bBottom = yBaseline + fontMetrics.bottom;

        /**
         * canvas由很多层画布组成。因此这里可以定义多层画布，canvas.save()和canvas.restore()之间算一层画布
         */
        // 绘制左边的矩形
        canvas.save();
        Rect rectLeft = new Rect((int)lLeft, (int)tTop, (int)left, (int)bBottom);
        canvas.clipRect(rectLeft);  // 裁剪，只画矩形区域
        canvas.drawText(mText, xBaseline, yBaseline, paint);
        canvas.restore();

        // 绘制上面的矩形
        canvas.save();
        Rect rectTop = new Rect((int)left, (int)tTop, (int)right, (int)top);
        canvas.clipRect(rectTop);
        canvas.drawText(mText, xBaseline, yBaseline, paint);
        canvas.restore();

        // 绘制右边的矩形
        canvas.save();
        Rect rectRight = new Rect((int)right, (int)tTop, (int)rRight, (int)bBottom);
        canvas.clipRect(rectRight);
        canvas.drawText(mText, xBaseline, yBaseline, paint);
        canvas.restore();

        // 绘制下面的矩形
        canvas.save();
        Rect rectBottom = new Rect((int)left, (int)bottom, (int)right, (int)bBottom);
        canvas.clipRect(rectBottom);
        canvas.drawText(mText, xBaseline, yBaseline, paint);
        canvas.restore();

        // 绘制中间矩形区域
        canvas.save();
        paint.setColor(mShadowColor);
        Rect rect = new Rect((int)left, (int)top, (int)right, (int)bottom);
        canvas.clipRect(rect);  // 裁剪，只画矩形区域
        canvas.drawText(mText, xBaseline, yBaseline, paint);
        canvas.restore();
    }

    // 定义出5个属性，供在外面这几设置
    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int RIGHT = 4;
    public static final int BOTTOM = 8;
    // 1 -> 0001    left|bottom -> 1001     left|right -> 0101，由于switch中没有这个值，默认为center
    // 2 -> 0010
    // 4 -> 0100
    // 8 -> 1000

    public void setGravity(int gravity) {
        this.mGravity = gravity;
        invalidate();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
        invalidate();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidate();
    }

    public float getLeftPercent() {
        return leftPercent;
    }

    public void setLeftPercent(float leftPercent) {
        this.leftPercent = leftPercent;
        invalidate();
    }

    public float getRightPercent() {
        return rightPercent;
    }

    public void setRightPercent(float rightPercent) {
        this.rightPercent = rightPercent;
        invalidate();
    }

    public float getTopPercent() {
        return topPercent;
    }

    public void setTopPercent(float topPercent) {
        this.topPercent = topPercent;
        invalidate();
    }

    public float getBottomPercent() {
        return bottomPercent;
    }

    public void setBottomPercent(float bottomPercent) {
        this.bottomPercent = bottomPercent;
        invalidate();
    }

    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }
}
