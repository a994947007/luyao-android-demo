package com.hc.android_demo.dialog.bottom.sheet;

import android.view.View;
import com.hc.android_demo.R;
import com.hc.my_views.bottomsheet.HalfBottomSheetView;
import com.hc.support.mvps.Presenter;
import com.hc.util.ViewUtils;

public class HalfBottomSheetControlPresenter extends Presenter {
    private View headerView;
    private HalfBottomSheetDialogFragmentTest dialogFragmentTest;
    private HalfBottomSheetView mContainer;

    @Override
    protected void doInject() {
        super.doInject();
        dialogFragmentTest = inject(HalfBottomSheetDialogFragmentTest.class);
    }

    @Override
    public void doBindView(View rootView) {
        super.doBindView(rootView);
        headerView = rootView.findViewById(R.id.header);
        mContainer = rootView.findViewById(R.id.container);
    }

    @Override
    protected void onBind() {
        super.onBind();
        mContainer.setMaxHeight(ViewUtils.getDisplayHeight(getContext()) - ViewUtils.dp2px(20f));
        headerView.setOnClickListener(v -> {
            if (!mContainer.isFullExpanded) {
                mContainer.halfToFull();
            } else {
                mContainer.fullToHalf();
            }
        });
        mContainer.addOnStateChangeListener(state -> {
            if (state == HalfBottomSheetView.STATE_HIDDEN) {
                dialogFragmentTest.dismissAllowingStateLoss();
            }
        });
    }
}
