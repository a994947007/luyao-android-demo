package com.hc.my_views.textview.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ReplacementSpan extends Span{

    void draw(@NonNull Canvas canvas, CharSequence text,
              @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x,
              int top, int y, int bottom, @NonNull Paint paint);

    int getSize(@NonNull Paint paint, CharSequence text,
                @IntRange(from = 0) int start, @IntRange(from = 0) int end,
                @Nullable Paint.FontMetrics fm);

    @Override
    void updateDrawState(TextPaint tp);
}
