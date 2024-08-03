package com.hc.my_views.textview.span;

import android.graphics.Paint;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ReplacementSpan extends Span{

    int getSize(@NonNull Paint paint, CharSequence text,
                @IntRange(from = 0) int start, @IntRange(from = 0) int end,
                @Nullable Paint.FontMetrics fm);

}
