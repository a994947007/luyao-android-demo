package com.jny.react_native.component.wrapedit;

import androidx.annotation.Nullable;

import com.facebook.react.views.text.ReactTextViewManagerCallback;
import com.facebook.react.views.textinput.ReactTextInputShadowNode;
import com.facebook.yoga.YogaMeasureFunction;

public class WrapReactEditViewShadowNode extends ReactTextInputShadowNode implements YogaMeasureFunction {
    public WrapReactEditViewShadowNode(@Nullable @javax.annotation.Nullable ReactTextViewManagerCallback reactTextViewManagerCallback) {
        super(reactTextViewManagerCallback);
    }

    public WrapReactEditViewShadowNode() {
    }
}
