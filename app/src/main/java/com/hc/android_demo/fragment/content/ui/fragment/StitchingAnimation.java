package com.hc.android_demo.fragment.content.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.view.View;

import com.hc.util.ViewUtils;

public class StitchingAnimation {

    private final Rect fromPosition;
    private final Rect toPosition;
    private ValueAnimator expandAnimation;
    private ValueAnimator closeAnimation;
    private final View scaleView;

    public StitchingAnimation(Rect fromPosition, View toView) {
        this.fromPosition = fromPosition;
        toPosition = new Rect();
        scaleView = toView;
        int toViewWidth = ViewUtils.getDisplayWidth(toView.getContext());
        int displayHeight = ViewUtils.getDisplayHeight(toView.getContext());
        int toViewHeight = toViewWidth * fromPosition.height() / fromPosition.width();
        toPosition.left = 0;
        toPosition.top = (displayHeight - toViewHeight) / 2;
        toPosition.right = toViewWidth;
        toPosition.bottom = (displayHeight + toViewHeight) / 2;
        initAnimation();
    }

    private void initAnimation() {
        expandAnimation = new ValueAnimator();
        expandAnimation.setFloatValues(0f, 1f);
        expandAnimation.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            updateScaleView(scale);
        });
        expandAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                updateScaleView(1f);
            }
        });
        expandAnimation.setDuration(200);

        closeAnimation = new ValueAnimator();
        closeAnimation.setFloatValues(1f, 0f);
        closeAnimation.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            updateScaleView(scale);
        });
        closeAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                updateScaleView(0f);
            }
        });
        closeAnimation.setDuration(200);
    }

    private void updateScaleView(float scale) {
        int currentWidth = (int) (fromPosition.width() + (toPosition.width() - fromPosition.width()) * scale);
        int currentHeight = (int) (fromPosition.height() + (toPosition.height() - fromPosition.height()) * scale);
        int translationX = (int) (fromPosition.left + (toPosition.left - fromPosition.left) * scale);
        int translationY = (int) (fromPosition.top + (toPosition.top - fromPosition.top) * scale);
        scaleView.setTranslationX(translationX);
        scaleView.setTranslationY(translationY);
        updateSize(scaleView, currentWidth, currentHeight);
    }

    public void open() {
        if (!expandAnimation.isStarted()) {
            expandAnimation.start();
        }
    }

    public void close() {
        if (!closeAnimation.isStarted()) {
            closeAnimation.start();
        }
    }

    public void addExpandAnimatorListener(Animator.AnimatorListener animatorListener) {
        if (expandAnimation != null) {
            expandAnimation.addListener(animatorListener);
        }
    }

    public void addCollapseAnimatorListener(Animator.AnimatorListener animatorListener) {
        if (closeAnimation != null) {
            closeAnimation.addListener(animatorListener);
        }
    }

    public void addExpandUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        if (expandAnimation != null) {
            expandAnimation.addUpdateListener(animatorUpdateListener);
        }
    }

    public void addCollapseUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        if (closeAnimation != null) {
            closeAnimation.addUpdateListener(animatorUpdateListener);
        }
    }

    public void updateSize(View view, int width, int height) {
        if (view.getWidth() != width && view.getHeight() != height) {
            view.getLayoutParams().width = width;
            view.getLayoutParams().height = height;
            view.requestLayout();
        }
    }
}
