package com.jny.react_native.component.wrapedit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.views.textinput.ReactTextInputLocalData;
import com.jny.common.component.SimpleTextWatcher;
import com.jny.react_native.R;

public class WrapReactEditView extends FrameLayout {

    private View mContentView;
    private EditText mEditText;

    public WrapReactEditView(@NonNull Context context) {
        super(context);
        initView();
    }

    public WrapReactEditView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WrapReactEditView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.react_edit_layout, this, true);
        mEditText = mContentView.findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onContentChange();
            }
        });
    }

    public EditText getEditText() {
        return mEditText;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.onContentChange();
    }

    private void onContentChange() {
        ReactContext reactContext = UIManagerHelper.getReactContext(this);
        ReactTextInputLocalData localData = new ReactTextInputLocalData(mEditText);
        UIManagerModule uiManager = (UIManagerModule)reactContext.getNativeModule(UIManagerModule.class);
        if (uiManager != null) {
            uiManager.setViewLocalData(this.getId(), localData);
        }
    }
}
