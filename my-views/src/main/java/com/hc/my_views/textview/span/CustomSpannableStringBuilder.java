package com.hc.my_views.textview.span;

import java.util.ArrayList;
import java.util.List;

public class CustomSpannableStringBuilder {

    private final StringBuilder textBuilder = new StringBuilder();
    private final List<SpanItem> spanList = new ArrayList<>();

    public CustomSpannableStringBuilder appendText(String text) {
        textBuilder.append(text);
        return this;
    }

    public CustomSpannableStringBuilder addSpan(Span span, int startIndex, int endIndex) {
        if (!checkSpan(span, startIndex, endIndex)) {
            return this;
        }
        spanList.add(new SpanItem(
                span,
                startIndex,
                endIndex
        ));
        return this;
    }

    /**
     * ReplacementSpan 不支持覆盖到已有Span位置
     */
    private boolean checkSpan(Span span, int startIndex, int endIndex) {
        if (!(span instanceof ReplacementSpan)) {
            return true;
        }
        for (SpanItem spanItem : spanList) {
            if (startIndex > spanItem.startIndex && startIndex <  spanItem.endIndex) {
                return false;
            }
            if (endIndex < spanItem.endIndex && endIndex > spanItem.startIndex) {
                return false;
            }
        }
        return true;
    }

    public String getText() {
        return textBuilder.toString();
    }

    public List<SpanItem> getSpanList() {
        return this.spanList;
    }

}
