package com.hc.my_views.textview.span;

import android.text.TextPaint;

public abstract class BaseReplacementSpan implements ReplacementSpan{

    @Override
    public void updateDrawState(TextPaint tp) { }

    @Override
    public void reset(TextPaint tp) { }

}
