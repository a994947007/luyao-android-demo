package com.hc.my_views.textview.span;

import android.text.TextPaint;

public interface Span {

    void updateDrawState(TextPaint tp);

    void reset(TextPaint tp);
}
