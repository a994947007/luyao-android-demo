package com.hc.android_demo.fragment.content.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.hc.android_demo.R;
import com.hc.base.activity.ActivityStarter;
import com.hc.my_views.coordinateLayout.Behavior;
import com.hc.util.ViewUtils;
import com.jny.common.fragment.FragmentConstants;

@AutoService({ActivityStarter.class})
public class CoordinateLayoutFragment extends Fragment implements ActivityStarter {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coordinate_layout, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv = view.findViewById(R.id.tv);
        tv.setText("打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" +
                "打分卡丽娜第三方发打发斯蒂芬大师傅打法萨芬撒到主线程那就看呗请叫我考核人几哈接口或会计证程序" );
    }

    @NonNull
    @Override
    public Fragment getContentFragment() {
        return new CoordinateLayoutFragment();
    }

    @Override
    public String getStarterId() {
        return FragmentConstants.COORDINATE_TEST_FRAGMENT_ID;
    }

    public static class ImageViewBehavior extends Behavior {
        private int minHeight = ViewUtils.dp2px(50);
        private int originalHeight = 0;
        private int currentHeight = 0;

        public ImageViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void onFinishInflate(View parent, View child) {
            super.onFinishInflate(parent, child);
            originalHeight = child.getMeasuredHeight();
            currentHeight = originalHeight;
        }

        @Override
        public void onNestedPreScroll(View parent, View child, View target, int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(parent, child, target, dx, dy, consumed);
            // ViewCompat.offsetTopAndBottom(view, offsetTop - (view.getTop() - layoutTop));
            // 这里应该使用viewOffsetHelper，如果使用LayoutParams改变高度，会导致dy出现负数，画面抖动的情况
            if (dy > 0 && currentHeight > minHeight) {
                int cutHeight = 0;
                if (dy <= currentHeight - minHeight) {
                    cutHeight = dy;
                } else {
                    cutHeight = currentHeight - minHeight;
                }
                currentHeight -= cutHeight;
                ViewCompat.offsetTopAndBottom(child, -cutHeight);
                ViewCompat.offsetTopAndBottom(target, -cutHeight);
                consumed[1] = cutHeight;
            } else if (dy < 0 && currentHeight < originalHeight && target.getScrollY() == 0) {
                int cutHeight = 0;
                if (Math.abs(dy) > originalHeight - currentHeight) {
                    cutHeight = currentHeight - originalHeight;
                } else {
                    cutHeight = dy;
                }
                currentHeight += -cutHeight;
                ViewCompat.offsetTopAndBottom(child, -cutHeight);
                ViewCompat.offsetTopAndBottom(target, -cutHeight);
                consumed[1] = cutHeight;
            }
            Log.d("Coordinate", "" + dy);

            /* 不能使用下面这种方案，通过修改LayoutParams的height来折叠View，这会使得dy有时候出现负数，出现抖动现象。
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            if (dy > 0 && lp.height > minHeight) {   // 往上滑动，并且还能滑
                int cutHeight = 0;
                if (dy <= lp.height - minHeight) {
                    cutHeight = dy;
                    lp.height = lp.height - dy;
                } else {
                    cutHeight = lp.height - minHeight;
                    lp.height = minHeight;
                }
                child.setLayoutParams(lp);
                consumed[1] = cutHeight;
            }else if (dy < 0 && lp.height < originalHeight) { // 往下滑动，并且还能滑
                int cutHeight = 0;
                if (Math.abs(dy) >= originalHeight - lp.height) {
                    cutHeight = originalHeight - lp.height;
                    lp.height = originalHeight;
                } else {
                    cutHeight = dy;
                    lp.height = lp.height + Math.abs(dy);
                }
                child.setLayoutParams(lp);
                consumed[1] = cutHeight;
            }
            */
        }
    }
}
