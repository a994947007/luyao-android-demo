package com.jny.react_native.component.wrapedit;

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
import com.jny.react_native.component.edit.CustomReactEditView;

public class WrapReactEditViewManager extends BaseViewManager<WrapReactEditView, LayoutShadowNode> {
    @NonNull
    @Override
    public String getName() {
        return "RCTWrapReactEditView";
    }

    @NonNull
    @Override
    protected WrapReactEditView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new WrapReactEditView(reactContext);
    }

    @Override
    public void updateExtraData(@NonNull WrapReactEditView view, Object extraData) {
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
                TextInlineImageSpan.possiblyUpdateInlineImageSpans(spannable, view.getEditText());
            }
        }
    }

    public ReactBaseTextShadowNode createShadowNodeInstance() {
        return new WrapReactEditViewShadowNode();
    }

    public ReactBaseTextShadowNode createShadowNodeInstance(@Nullable ReactTextViewManagerCallback reactTextViewManagerCallback) {
        return new WrapReactEditViewShadowNode(reactTextViewManagerCallback);
    }

    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return WrapReactEditViewShadowNode.class;
    }
}
