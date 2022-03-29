package com.jny.react_native.component;

import com.facebook.react.views.text.ReactBaseTextShadowNode;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaNode;

public class CustomReactEditViewShadowNode extends ReactBaseTextShadowNode implements YogaMeasureFunction {
    @Override
    public long measure(YogaNode node, float width, YogaMeasureMode widthMode, float height, YogaMeasureMode heightMode) {
        return 0;
    }
}
