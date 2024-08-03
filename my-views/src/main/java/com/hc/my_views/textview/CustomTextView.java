package com.hc.my_views.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hc.my_views.textview.span.ReplacementSpan;
import com.hc.my_views.textview.span.SpanItem;
import com.hc.my_views.textview.span.SpannableStringBuilder;
import com.jny.android.demo.base_util.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 自定义TextView，支持自定义Span
 */
public class CustomTextView extends View {

    private String text;
    private final List<SpanItem> spanList = new ArrayList<>();
    private final List<SpanItem> replacementSpans = new ArrayList<>();
    private Paint paint;
    private int textSize;

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context) {
        this(context, null);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(textSize);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(SpannableStringBuilder spannableStringBuilder) {
        this.text = spannableStringBuilder.getText();
        this.spanList.clear();
        this.replacementSpans.clear();
        this.spanList.addAll(spannableStringBuilder.getSpanList());
        for (SpanItem spanItem : this.spanList) {
            if (spanItem.span instanceof ReplacementSpan) {
                replacementSpans.add(spanItem);
            }
        }
        Collections.sort(
                replacementSpans, new Comparator<SpanItem>() {
                    @Override
                    public int compare(SpanItem span1, SpanItem span2) {
                        return span1.startIndex - span2.startIndex;
                    }
                }
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureTextWidth = 0;
        int measureTextHeight = 0;
        if (text != null) {
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            measureTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);
            int textIndex = 0;
            for (SpanItem replacementSpan : replacementSpans) {
                String str = text.substring(textIndex, replacementSpan.startIndex);
                // 加上span之前的
                if (!TextUtils.isEmpty(str)) {
                    measureTextWidth += (int) paint.measureText(str);
                }
                // 加上span的宽度
                measureTextWidth += ((ReplacementSpan) replacementSpan.span)
                        .getSize(paint, text, replacementSpan.startIndex, replacementSpan.endIndex, fontMetrics);
                textIndex = replacementSpan.endIndex;
            }
            // 加上最后一个span的宽度
            if (textIndex != text.length()) {
                String str = text.substring(textIndex);
                if (!TextUtils.isEmpty(str)) {
                    measureTextWidth += (int) paint.measureText(str);
                }
            }
        }
        setMeasuredDimension(measureTextWidth, measureTextHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (text != null) {

        }
    }
}
