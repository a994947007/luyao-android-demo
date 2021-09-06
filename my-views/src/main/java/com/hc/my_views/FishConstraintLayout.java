package com.hc.my_views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.hc.drawable.FishDrawable;

public class FishConstraintLayout extends ConstraintLayout{
    private Paint mPaint;
    private ImageView ivFish;
    private FishDrawable mFishDrawable;
    private float touchX;
    private float touchY;
    private float ripple;
    private int alpha;

    public FishConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public FishConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);  // 由于ViewGroup默认不执行onDraw，这里需要让其Draw

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        // 设置成非填充
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        ivFish = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivFish.setLayoutParams(layoutParams);
        mFishDrawable = new FishDrawable();
        ivFish.setImageDrawable(mFishDrawable);
        addView(ivFish);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "ripple", 0, 1f).setDuration(1000);
        objectAnimator.start();
        
        makeTrail();

        return super.onTouchEvent(event);
    }

    /**
     * 鱼的行走路线为三阶贝塞尔曲线，需要根据鱼的方向画线
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeTrail() {
        // 鱼的重心，相对于ImageView
        PointF middlePoint = mFishDrawable.getMiddlePoint();
        PointF headPoint = mFishDrawable.getHeaderPoint();
        // 鱼的绝对坐标，起始点
        PointF fishMiddle = new PointF(ivFish.getX() + middlePoint.x, ivFish.getY() + middlePoint.y);
        // 鱼头坐标，控制点1
        PointF fishHead = new PointF(ivFish.getX() + headPoint.x, ivFish.getY() + headPoint.y);

        PointF touchPoint = new PointF(touchX, touchY);
        // 控制点2
        float angle = includeAngle(fishMiddle, fishHead, touchPoint) / 2;
        float delta = includeAngle(fishMiddle, new PointF(fishMiddle.x +1, fishMiddle.y), fishHead);
        PointF controlPoint = mFishDrawable.calculatePoint(fishMiddle, mFishDrawable.getHEAD_RADIUS() * 1.6f, angle + delta);
        Path path = new Path();
        path.moveTo(fishMiddle.x - middlePoint.x, fishMiddle.y - middlePoint.y);
        path.cubicTo(fishHead.x - middlePoint.x, fishHead.y - middlePoint.y,
                controlPoint.x - middlePoint.x, controlPoint.y - middlePoint.y,
                touchX - middlePoint.x, touchY - middlePoint.y);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivFish, "x", "y", path);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFishDrawable.setFrequency(1f);
                mFishDrawable.stopSwim();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFishDrawable.setFrequency(2f);
                mFishDrawable.startSwim();
            }
        });

        PathMeasure pathMeasure = new PathMeasure(path, false);
        float [] tan = new float[2];
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();  // 执行整个周期的百分之多少
                pathMeasure.getPosTan(pathMeasure.getLength() * animatedFraction, null, tan);   // 将当前位置的x、y坐标放到tan数组中
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                mFishDrawable.setAngle(angle);
            }
        });
        objectAnimator.start();
    }

    public float includeAngle(PointF O, PointF A, PointF B) {
        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        float OALength = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
        float OBLength = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));
        float cosAOB = AOB / (OALength * OBLength);
        float angle = (float) Math.toDegrees(Math.acos(cosAOB));
        float direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);
        if (direction == 0) {
            if (angle >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (direction > 0) {
                return -angle;
            } else {
                return angle;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAlpha(alpha);
        canvas.drawCircle(touchX, touchY, ripple * 150, mPaint);
    }

    public void setRipple(float ripple) {
        this.alpha = (int) (100 *( 1 - ripple));
        this.ripple = ripple;
        invalidate();
    }
}
