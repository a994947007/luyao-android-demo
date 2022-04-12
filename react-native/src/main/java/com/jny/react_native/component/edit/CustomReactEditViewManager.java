package com.jny.react_native.component.edit;

import android.text.Spannable;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.views.text.ReactBaseTextShadowNode;
import com.facebook.react.views.text.ReactTextUpdate;
import com.facebook.react.views.text.ReactTextViewManagerCallback;
import com.facebook.react.views.text.TextInlineImageSpan;
import com.jny.react_native.R;

public class CustomReactEditViewManager extends BaseViewManager<CustomReactEditView, LayoutShadowNode> {
    @NonNull
    @Override
    public String getName() {
        return "RCTCustomReactEditView";
    }

    @NonNull
    @Override
    protected CustomReactEditView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return (CustomReactEditView) LayoutInflater.from(reactContext).inflate(R.layout.custom_edit_text, null, false);
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

    public ReactBaseTextShadowNode createShadowNodeInstance() {
        return new CustomReactEditViewShadowNode();
    }

    public ReactBaseTextShadowNode createShadowNodeInstance(@Nullable ReactTextViewManagerCallback reactTextViewManagerCallback) {
        return new CustomReactEditViewShadowNode(reactTextViewManagerCallback);
    }

    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return CustomReactEditViewShadowNode.class;
    }
}
