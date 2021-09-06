package com.hc.my_views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿微信通讯录右边字母列表
 */
public class LetterSlideBar extends View {
    protected int letterPadding = 0;    // px
    protected int textSize = 8; // px
    private final Paint bluePaint = new Paint();
    private final Paint blackPaint = new Paint();
    private final Paint whitePaint = new Paint();
    private final List<String> letters = new ArrayList<>(26);
    private final List<Integer> letterWidth = new ArrayList<>();
    private int mTextHeight;
    private int mIndex = 0; // 当前点击的字母
    private OnTouchLetterListener onTouchLetterListener;

    public interface OnTouchLetterListener {
        void onTouch(View view, String letter);
    }

    public LetterSlideBar(Context context) {
        this(context, null);
    }

    public LetterSlideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSlideBar);
        letterPadding = (int) typedArray.getDimension(R.styleable.LetterSlideBar_letterPadding, letterPadding);
        textSize = (int) typedArray.getDimension(R.styleable.LetterSlideBar_android_textSize, textSize);
        init();
        typedArray.recycle();
    }

    private void init() {
        letters.add("#");
        for (char c = 'A'; c <= 'Z'; c++) {
            letters.add("" + c);
        }

        bluePaint.setColor(Color.BLUE);
        bluePaint.setDither(true);
        bluePaint.setAntiAlias(true);
        bluePaint.setTextSize(textSize);

        blackPaint.setColor(Color.BLACK);
        blackPaint.setDither(true);
        blackPaint.setAntiAlias(true);
        blackPaint.setTextSize(textSize);

        whitePaint.setColor(Color.WHITE);
        whitePaint.setDither(true);
        whitePaint.setAntiAlias(true);
        whitePaint.setTextSize(textSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 计算当前点击的是哪个字母
                int index = (int) ((event.getY() - getPaddingTop() - getPaddingBottom() + letterPadding) / (mTextHeight + letterPadding));
                if (index > 0 && index != mIndex) {
                    invalidate();
                }
                mIndex = index;
                onTouch(this, letters.get(mIndex));
        }
        return true;
    }

    protected void onTouch(View v, String letter) {
        if (onTouchLetterListener != null) {
            onTouchLetterListener.onTouch(this, letter);
        }
    }

    public void setOnClickListener(OnTouchLetterListener listener) {
        this.onTouchLetterListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int textWidth = (int) blackPaint.measureText("W");
        int width = getPaddingLeft() + textWidth + getPaddingRight();
        Paint.FontMetrics fontMetrics = blackPaint.getFontMetrics();
        mTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);
        int height = mTextHeight * letters.size() + letterPadding * (letters.size() - 1) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
        for (String letter : letters) {
            letterWidth.add((int) blackPaint.measureText(letter));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetrics fontMetrics = blackPaint.getFontMetrics();
        int baseline = getPaddingTop();
        for (int i = 0; i < letters.size(); i++) {
            baseline += mTextHeight;
            int x = getWidth() / 2 - letterWidth.get(i) / 2;
            if (i == mIndex && i > 0) {
                canvas.drawCircle((float) getWidth() / 2, baseline - (float)mTextHeight / 2, (float) getWidth() / 2, bluePaint);
                canvas.drawText(letters.get(i), x, baseline - fontMetrics.bottom, whitePaint);
            } else {
                canvas.drawText(letters.get(i), x, baseline - fontMetrics.bottom, blackPaint);
            }
            baseline += letterPadding;
        }
    }

    static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
