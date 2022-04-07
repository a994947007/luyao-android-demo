package com.jny.react_native.component.edit;

import androidx.annotation.Nullable;

import com.facebook.react.views.text.ReactTextViewManagerCallback;
import com.facebook.react.views.textinput.ReactTextInputShadowNode;
import com.facebook.yoga.YogaMeasureFunction;

public class CustomReactEditViewShadowNode extends ReactTextInputShadowNode implements YogaMeasureFunction {
    public CustomReactEditViewShadowNode(@Nullable @javax.annotation.Nullable ReactTextViewManagerCallback reactTextViewManagerCallback) {
        super(reactTextViewManagerCallback);
    }

    public CustomReactEditViewShadowNode() {
    }
}
