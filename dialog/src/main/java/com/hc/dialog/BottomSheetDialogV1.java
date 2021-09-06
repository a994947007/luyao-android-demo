package com.hc.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hc.util.InflaterUtils;

public class BottomSheetDialogV1 extends BottomSheetDialog {
    private View contentView;
    private Activity mActivity;
    public BottomSheetDialogV1(Context context) {
        super(context);
        this.mActivity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        contentView = InflaterUtils.inflate(mActivity, R.layout.bsd_v1_layout);
        this.setContentView(this.contentView);
    }
}
