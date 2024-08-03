package com.hc.my_views.textview.span;

public class SpanItem {

    public Span span;
    public int startIndex;
    public int endIndex;

    public SpanItem(
            Span span,
            int startIndex,
            int endIndex
    ) {
        this.span = span;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}
