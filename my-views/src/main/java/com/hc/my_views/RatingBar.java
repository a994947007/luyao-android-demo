package com.hc.my_views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class RatingBar extends View {
    protected int mStarNum;
    protected int mStarPadding;
    protected Bitmap mStarNormal;
    protected Bitmap mStarFocus;
    protected Bitmap mZipStarNormal;
    protected Bitmap mZipStarFocus;
    private int mStarWidth;
    private int mStarActualNum = 0;

    public RatingBar(Context context) {
        super(context);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttrs(context, attrs);
    }

    public RatingBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        mStarNum = typedArray.getInteger(R.styleable.RatingBar_starNum, 5);
        int starNormalId = typedArray.getResourceId(R.styleable.RatingBar_starNormal, 0);
        if (starNormalId == 0) {
            throw new RuntimeException("属性starNormal不能为空");
        }
        int starFocusId = typedArray.getResourceId(R.styleable.RatingBar_starFocus, 0);
        if (starFocusId == 0) {
            throw new RuntimeException("属性starFocusId不能为空");
        }
        mStarNormal = BitmapFactory.decodeResource(getResources(), starNormalId);
        mStarFocus = BitmapFactory.decodeResource(getResources(), starFocusId);
        mStarPadding = dp2px(typedArray.getDimension(R.styleable.RatingBar_starPadding, 0));
        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                int starActualNum;
                if (x < (float)mStarPadding / 2 + mStarWidth + getPaddingLeft()) {
                    starActualNum = 1;
                } else if (x > getMeasuredWidth() - mStarWidth - (float)mStarPadding / 2 - getPaddingRight()) {
                    starActualNum = mStarNum;
                } else {
                    starActualNum = (int)Math.ceil((x - (float)mStarPadding / 2 - mStarWidth - getPaddingLeft()) / (mStarWidth + mStarPadding)) + 1;
                }
                if (starActualNum != mStarActualNum) {
                    invalidate();
                }
                mStarActualNum = starActualNum;
        }
        return true;        // 这里需要返回true，如果返回false表示不消费，Down事件用于找到对应的消费者，如果返回false，那么就不会被记录在事件消费链中
    }

    /**
     * 计算规则，取(width + starPadding - paddingLeft - paddingRight)/mStartNum-starPadding来缩放star，height不起缩放作用，但能控制超过star高度时整个View的高度。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 如果width是wrap_content，则直接用原图
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            int height = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY ?
                    mStarNormal.getHeight() : Math.max(mStarNormal.getHeight(), MeasureSpec.getSize(heightMeasureSpec));
            mZipStarNormal = mStarNormal;
            mZipStarFocus = mStarFocus;
            int width = mZipStarFocus.getWidth() * mStarNum + mStarPadding * (mStarNum - 1);
            mStarWidth = mZipStarNormal.getWidth();
            setMeasuredDimension(width, height);
            return;
        }
        // 如果width是exactly或者match_parent（也是exactly），则需要计算(width + starPadding)/mStartNum-starPadding，来缩放
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mStarWidth = (width - getPaddingLeft() - getPaddingRight() - mStarPadding * (mStarNum - 1)) / mStarNum;
        float scale = (float) mStarWidth / mStarNormal.getWidth();
        int starHeight = (int) (mStarNormal.getHeight() * scale);
        mZipStarNormal = zipBitmap(mStarNormal, mStarWidth, starHeight);
        mZipStarFocus = zipBitmap(mStarFocus, mStarWidth, starHeight);
        setMeasuredDimension(width, starHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制评分星星
        drawStar(mZipStarFocus, 0, mStarActualNum, canvas);
        // 绘制背景星星
        drawStar(mZipStarNormal, mStarActualNum, mStarNum, canvas);
    }

    private void drawStar(Bitmap star,int start, int num, Canvas canvas) {
        // 计算开始位置距离最左边的距离
        int left = 0;
        if (start == 0) {
            left = getPaddingLeft();
        } else {
            left = getPaddingLeft() + mStarWidth * start + mStarPadding * start;
        }

        for (int i = start; i < num; i++) {
            canvas.drawBitmap(star, left, getPaddingTop(), null);
            left += mStarWidth + mStarPadding;
        }
    }

    //放大缩小图片
    public static Bitmap zipBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)w / width);
        float scaleHeight = ((float)h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }


    static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
