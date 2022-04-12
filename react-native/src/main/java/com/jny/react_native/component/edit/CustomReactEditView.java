package com.jny.react_native.component.edit;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.views.textinput.ReactTextInputLocalData;
import com.jny.common.component.SimpleTextWatcher;

public class CustomReactEditView extends AppCompatEditText {
    public CustomReactEditView(Context context) {
        super(context);
        initView();
    }

    public CustomReactEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomReactEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onContentChange();
            }
        });
    }

    public boolean isLayoutRequested() {
        return false;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.onContentChange();
    }

    private void onContentChange() {
        ReactContext reactContext = UIManagerHelper.getReactContext(this);
        ReactTextInputLocalData localData = new ReactTextInputLocalData(this);
        UIManagerModule uiManager = (UIManagerModule)reactContext.getNativeModule(UIManagerModule.class);
        if (uiManager != null) {
            uiManager.setViewLocalData(this.getId(), localData);
        }
    }
}
