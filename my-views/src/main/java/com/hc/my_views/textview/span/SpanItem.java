package com.hc.my_views.textview.span;

import android.text.style.CharacterStyle;

public class SpanItem {

    public CharacterStyle span;
    public int startIndex;
    public int endIndex;

    public SpanItem(
            CharacterStyle span,
            int startIndex,
            int endIndex
    ) {
        this.span = span;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}
