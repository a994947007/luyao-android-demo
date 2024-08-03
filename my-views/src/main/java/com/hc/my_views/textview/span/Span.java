package com.hc.my_views.textview.span;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public interface Span {

    void draw(@NonNull Canvas canvas, CharSequence text,
              @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x,
              int top, int y, int bottom, @NonNull Paint paint);

}
