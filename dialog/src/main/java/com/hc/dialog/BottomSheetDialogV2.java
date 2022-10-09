package com.hc.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jny.android.demo.base_util.InflaterUtils;
import com.jny.android.demo.base_util.ThreadUtils;

/**
 * 通过设置Behavior的peekHeight来展示展示的dialog高度
 */
public class BottomSheetDialogV2 extends BottomSheetDialog {
    private Activity mActivity;
    private View contentView;

    public BottomSheetDialogV2(@NonNull Context context) {
        super(context);
        this.mActivity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        contentView = InflaterUtils.inflate(mActivity, R.layout.bsd_v2_layout);
        setContentView(contentView);
        /**
         * 通过设置behavior的peekHeight和state来控制展示多高，我们也可以通过其他方式控制，如加载完成后，需要将面板expand
         */
        final BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(contentView.getRootView().findViewById(R.id.design_bottom_sheet));
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 有多种状态
        behavior.setPeekHeight(dp2px(80));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int i) {
                /**
                 * 如果已经完全展开了，下次就直接影藏调了，不需要只展示一半了
                 */
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setPeekHeight(0);
                }
                /**
                 * 如果滑到低，dialog还是STATE_COLLAPSED状态，此时直接调用dialog.cancel()
                 */
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED && behavior.getPeekHeight() == 0) {
                    cancel();
                }
                Log.d("behavior", String.valueOf(behavior.getState()));
            }

            @Override
            public void onSlide(View view, float v) {}
        });

        /**
         * 模拟下载，下载完成之后，展示整个面板的内容
         */
        ThreadUtils.runOnNewThread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadUtils.runInMainThread(() -> {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            });
        });
    }

    public int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue, Resources.getSystem().getDisplayMetrics());
    }
}
