package com.hc.my_views.textview.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hc.util.ViewUtils;

public class IconSpan extends BaseReplacementSpan{

    private final Drawable drawable;

    public IconSpan(Drawable drawable) {
        this.drawable = drawable;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        this.drawable.setBounds(0, 0, ViewUtils.dp2px(15),ViewUtils.dp2px(15));
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetrics fm) {
        Drawable d = this.drawable;
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            Drawable b = this.drawable;
            canvas.save();

            int transY = bottom - b.getBounds().bottom;
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
    }
}
