package com.hc.my_views.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.hc.my_views.textview.span.SpanItem;
import com.hc.my_views.textview.span.CustomSpannableStringBuilder;
import com.hc.util.ViewUtils;
import com.jny.android.demo.base_util.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义TextView，支持自定义Span
 */
public class CustomTextView extends View {

    private String text;
    private final List<SpanItem> spanList = new ArrayList<>();
    private final List<SpanItem> replacementSpans = new ArrayList<>();
    private final Map<Integer, List<SpanItem>> spanItemMap = new HashMap<>();
    private TextPaint textPaint;
    private int textSize;
    private TextPaint workPaint;
    private Map<Integer, List<SpanItem>> remainItems = new HashMap<>();

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context) {
        this(context, null);
    }

    private void init() {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        if (textSize == 0) {
            textSize = ViewUtils.dp2px(15);
        }
        textPaint.setTextSize(textSize);
        workPaint = new TextPaint();
        workPaint.set(textPaint);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(CustomSpannableStringBuilder customSpannableStringBuilder) {
        this.text = customSpannableStringBuilder.getText();
        this.spanList.clear();
        this.replacementSpans.clear();
        this.spanList.addAll(customSpannableStringBuilder.getSpanList());
        for (SpanItem spanItem : this.spanList) {
            if (spanItem.span instanceof ReplacementSpan) {
                replacementSpans.add(spanItem);
            }
            List<SpanItem> spanItems = spanItemMap.get(spanItem.startIndex);
            if (spanItems == null) {
                spanItems = new ArrayList<>();
                spanItemMap.put(spanItem.startIndex, spanItems);
            }
            spanItems.add(spanItem);
        }
        Collections.sort(
                replacementSpans, new Comparator<SpanItem>() {
                    @Override
                    public int compare(SpanItem span1, SpanItem span2) {
                        return span1.startIndex - span2.startIndex;
                    }
                }
        );
        Collections.sort(
                spanList,
                new Comparator<SpanItem>() {
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
            Paint.FontMetricsInt fontMetricsInt = workPaint.getFontMetricsInt();
            measureTextHeight = fontMetricsInt.bottom - fontMetricsInt.top;
            int textIndex = 0;
            for (SpanItem replacementSpan : replacementSpans) {
                String str = text.substring(textIndex, replacementSpan.startIndex);
                // 加上span之前的
                if (!TextUtils.isEmpty(str)) {
                    measureTextWidth += (int) workPaint.measureText(str);
                }
                // 加上span的宽度
                measureTextWidth += ((ReplacementSpan) replacementSpan.span)
                        .getSize(workPaint, text, replacementSpan.startIndex, replacementSpan.endIndex, fontMetricsInt);
                textIndex = replacementSpan.endIndex;
            }
            // 加上最后一个span的宽度
            if (textIndex != text.length()) {
                String str = text.substring(textIndex);
                if (!TextUtils.isEmpty(str)) {
                    measureTextWidth += (int) workPaint.measureText(str);
                }
            }
        }
        setMeasuredDimension(measureTextWidth, measureTextHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (text == null) {
            return;
        }
        TextPaint paint = workPaint;
        resetPaint(paint);
        int startTextIndex = 0;
        float x = 0;
        float textWidth = getMeasuredWidth();
        float xBaseline = (getWidth() >> 1) -  textWidth/ 2;
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        float yBaseline = (getHeight() >> 1) - (float) (fontMetricsInt.ascent + fontMetricsInt.descent) /2;
        for (int i = 0; i < text.length(); i++) {
            List<SpanItem> remainSpanItems = remainItems.get(i);
            if (remainSpanItems != null && !remainSpanItems.isEmpty()) {
                // 先把之前的绘制了
                String str = text.substring(startTextIndex, i);
                canvas.save();
                canvas.drawText(str, x, yBaseline, paint);
                canvas.restore();
                x += paint.measureText(str);
                startTextIndex = i;
                // 再重置状态
                resetPaint(paint);
                remainItems.remove(i);
                for (Map.Entry<Integer, List<SpanItem>> spanItemEntry : remainItems.entrySet() ) {
                    List<SpanItem> spanItems = spanItemEntry.getValue();
                    for (SpanItem spanItem : spanItems) {
                        spanItem.span.updateDrawState(paint);
                    }
                }
            }

            List<SpanItem> spanItems = spanItemMap.get(i);
            if (spanItems != null) {
                String str = text.substring(startTextIndex, i);
                canvas.save();
                canvas.drawText(str, x, yBaseline, paint);
                canvas.restore();
                x += paint.measureText(str);
                startTextIndex = i;
                for (SpanItem spanItem : spanItems) {
                    str = text.substring(spanItem.startIndex, spanItem.endIndex);
                    CharacterStyle span = spanItem.span;
                    if (span instanceof ReplacementSpan) {
                        int spainWidth = ((ReplacementSpan) spanItem.span).getSize(workPaint, str, spanItem.startIndex, spanItem.endIndex, fontMetricsInt);
                        ((ReplacementSpan) spanItem.span).draw(canvas, str, spanItem.startIndex, spanItem.endIndex, x, 0, 0, getMeasuredHeight(), paint);
                        x += spainWidth;
                        startTextIndex = spanItem.endIndex;
                    } else {
                        span.updateDrawState(paint);
                        putSpanItem(remainItems, spanItem.endIndex, spanItem);
                    }
                }
            }
        }
        if (startTextIndex != text.length()) {
            String str = text.substring(startTextIndex);
            canvas.save();
            canvas.drawText(str, x, yBaseline, workPaint);
            canvas.restore();
        }
    }

    private void resetPaint(TextPaint paint) {
        paint.set(textPaint);
    }

    private void putSpanItem(Map<Integer, List<SpanItem>> spanItemMap, Integer index, SpanItem spanItem) {
        List<SpanItem> spanItems = spanItemMap.get(index);
        if (spanItems == null) {
            spanItems = new ArrayList<>();
        }
        spanItems.add(spanItem);
        spanItemMap.put(index, spanItems);
    }

    private void removeSpanItem(Map<Integer, List<SpanItem>> spanItemMap, Integer index, SpanItem spanItem) {
        List<SpanItem> spanItems = spanItemMap.get(index);
        if (spanItems != null) {
            spanItems.remove(spanItem);
            if (spanItems.isEmpty()) {
                spanItemMap.remove(index);
            }
        }
    }
}
