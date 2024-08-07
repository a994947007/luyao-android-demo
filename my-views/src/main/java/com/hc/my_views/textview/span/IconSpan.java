package com.hc.my_views.textview.span;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IconSpan extends ImageSpan {

    private final Drawable drawable;

    public IconSpan(Drawable drawable) {
        super(drawable);
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
        this.drawable = drawable;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        int lineHeight = fm.bottom - fm.top;
        Rect bounds = drawable.getBounds();
        int bitmapWidth = bounds.width();
        int bitmapHeight = bounds.height();

        int resultWidth = (int) ((float)lineHeight * bitmapWidth / bitmapHeight);
        bounds.set(new Rect(0, 0, resultWidth, lineHeight));
        return resultWidth;
    }
}
