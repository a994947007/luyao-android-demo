package com.hc.my_views.textview.span;

import android.text.TextPaint;

public class ColorSpan implements Span {

    private int color;
    private int originalColor;

    public ColorSpan(int color) {
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        originalColor = tp.getColor();
        tp.setColor(color);
    }

    @Override
    public void reset(TextPaint tp) {
        tp.setColor(originalColor);
    }
}
