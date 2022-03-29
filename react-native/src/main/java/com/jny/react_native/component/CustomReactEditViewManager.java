package com.jny.react_native.component;

import android.text.Spannable;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.text.ReactTextUpdate;
import com.facebook.react.views.text.TextInlineImageSpan;

public class CustomReactEditViewManager extends BaseViewManager<CustomReactEditView, LayoutShadowNode> {
    @NonNull
    @Override
    public String getName() {
        return "RCCustomReactEditView";
    }

    @NonNull
    @Override
    protected CustomReactEditView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new CustomReactEditView(reactContext);
    }

    @Override
    public void updateExtraData(@NonNull CustomReactEditView view, Object extraData) {
        if (extraData instanceof ReactTextUpdate) {
            ReactTextUpdate update = (ReactTextUpdate)extraData;
            int paddingLeft = (int)update.getPaddingLeft();
            int paddingTop = (int)update.getPaddingTop();
            int paddingRight = (int)update.getPaddingRight();
            int paddingBottom = (int)update.getPaddingBottom();
            if (paddingLeft != -1 || paddingTop != -1 || paddingRight != -1 || paddingBottom != -1) {
                view.setPadding(paddingLeft != -1 ? paddingLeft : view.getPaddingLeft(), paddingTop != -1 ? paddingTop : view.getPaddingTop(), paddingRight != -1 ? paddingRight : view.getPaddingRight(), paddingBottom != -1 ? paddingBottom : view.getPaddingBottom());
            }

            if (update.containsImages()) {
                Spannable spannable = update.getText();
                TextInlineImageSpan.possiblyUpdateInlineImageSpans(spannable, view);
            }
        }
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new CustomReactEditViewShadowNode();
    }

    @NonNull
    @Override
    public LayoutShadowNode createShadowNodeInstance(@NonNull ReactApplicationContext context) {
        return super.createShadowNodeInstance(context);
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return CustomReactEditViewShadowNode.class;
    }
}
