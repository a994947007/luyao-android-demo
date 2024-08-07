package com.hc.my_views.textview.span;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class ColorSpan extends CharacterStyle implements UpdateAppearance {

    private int color;

    public ColorSpan(int color) {
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(color);
    }
}
