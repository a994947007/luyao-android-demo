package com.hc.android_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hc.my_views.coordinateLayout.Behavior;
import com.hc.util.ViewUtils;

public class CoordinateLayoutActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate_layout);

        TextView tv = findViewById(R.id.tv);
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