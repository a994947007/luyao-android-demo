package com.jny.react_native.component.bottomsheet;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewManager;

public class ReactBottomSheetLayoutManager extends ViewGroupManager<ReactBottomSheetLayout> {
    @NonNull
    @Override
    public String getName() {
        return "ReactBottomSheetLayout";
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return ReactBottomSheetLayoutShadowNode.class;
    }

    @NonNull
    @Override
    protected ReactBottomSheetLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new ReactBottomSheetLayout(reactContext);
    }

    @Override
    public ReactBottomSheetLayoutShadowNode createShadowNodeInstance() {
        return new ReactBottomSheetLayoutShadowNode();
    }

    @Override
    public void updateExtraData(@NonNull ReactBottomSheetLayout root, Object extraData) {

    }

/*    @ReactProp(name = "maxHeight")
    public void setMaxHeight(ReactBottomSheetLayout root, int maxHeight) {
        root.getBottomSheetView().setMaxHeight(maxHeight);
    }

    @ReactProp(name = "height")
    public void setHeight(ReactBottomSheetLayout root, int height) {
        root.getBottomSheetView().getLayoutParams().height = height;
    }*/
}
