package com.hc.drawable;

import android.animation.ValueAnimator;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FishDrawable extends Drawable {

    private Path mPath;
    private Paint mPaint;

    private int OTHER_ALPHA = 110;
    private int BODY_ALPHA = 160;



    // 鱼的重心
    private PointF middlePoint;
    // 鱼头坐标
    private PointF headPoint;
    // 鱼的主要朝向角度
    private float fishMainAngle = 90;

    private float currentValue;

    /**
     * 鱼的长度值
     */

    private final float HEAD_RADIUS = 30;
    private final float BODY_LENGTH = HEAD_RADIUS * 3.2f;
    // 鱼的眼睛距离鱼头中心长度
    private float EYE_LENGTH = 0.7f * HEAD_RADIUS;
    // 鱼鳍起始点到head point的长度
    private float FINS_FISH_LENGTH = 0.9f * HEAD_RADIUS;
    // 鱼鳍的长度
    private float FIND_LENGTH = 1.3f * HEAD_RADIUS;
    // 节支大圆的半径
    private float BIG_RADIUS = 0.7f * HEAD_RADIUS;
    // 节肢中圆的半径
    private float MIDDLE_RADIUS = 0.6f * BIG_RADIUS;
    // 节肢小圆的半径
    private float SMALL_RADIUS = 0.4f * MIDDLE_RADIUS;
    // 中圆圆心位置的线长
    private float MIDDLE_CIRCLE_LENGTH = BIG_RADIUS * (0.6f + 1);
    // 小圆圆心位置的线长
    private float SMALL_CIRCLE_LENGTH = MIDDLE_RADIUS * (0.4f + 2.7f);
    // 三角形底边中心线长
    private float TRIANGLE_LENGTH = MIDDLE_RADIUS * 2.7f;
    // 眼睛的半径
    private float EYE_RADIUS = HEAD_RADIUS * 0.05f;

    public FishDrawable() {
        init();
    }

    private float frequency = 1f;

    private boolean isSwim = false;

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float fishAngle =  fishMainAngle + (float)Math.sin(Math.toRadians(currentValue * frequency)) * 5;
        // 鱼头的圆心坐标
        headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);
        // 右鱼鳍
        PointF rightFishPoint = calculatePoint(headPoint, FINS_FISH_LENGTH, fishAngle - 110);
        makeFins(canvas, rightFishPoint, fishAngle, true);
        // 左鱼鳍
        PointF leftFishPoint = calculatePoint(headPoint, FINS_FISH_LENGTH, fishAngle + 110);
        makeFins(canvas, leftFishPoint, fishAngle, false);
        // 节肢1
        PointF bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180);
        PointF middleCenterPoint = makeSegment(canvas, bodyBottomCenterPoint, fishAngle, MIDDLE_CIRCLE_LENGTH, BIG_RADIUS, MIDDLE_RADIUS,true);
        // 节肢2
        makeSegment(canvas, middleCenterPoint, fishAngle, SMALL_CIRCLE_LENGTH, MIDDLE_RADIUS, SMALL_RADIUS, false);
        float edgeLength = (float) Math.abs(Math.sin(Math.toRadians(currentValue * 1.5f * frequency)) * BIG_RADIUS);
        // 尾巴
        makeTriangle(canvas, middleCenterPoint, TRIANGLE_LENGTH, edgeLength, fishAngle);
        makeTriangle(canvas, middleCenterPoint, TRIANGLE_LENGTH - 15, edgeLength - 20, fishAngle);
        // 身体
        makeBody(canvas, headPoint, bodyBottomCenterPoint, fishAngle);
        // 眼睛
        makeEye(canvas, headPoint, fishAngle);
    }

    private void makeEye(Canvas canvas, PointF headPoint, float fishAngle) {
        PointF leftEye = calculatePoint(headPoint, EYE_LENGTH, fishAngle + 50);
        PointF rightEye = calculatePoint(headPoint, EYE_LENGTH, fishAngle - 50);
        canvas.drawCircle(leftEye.x, leftEye.y, EYE_RADIUS, mPaint);
        canvas.drawCircle(rightEye.x, rightEye.y, EYE_RADIUS, mPaint);
    }

    private void makeBody(Canvas canvas, PointF headPoint, PointF bodyBottomCenterPoint, float fishAngle) {
        PointF topLeft = calculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        PointF topRight = calculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90);
        PointF bottomLeft = calculatePoint(bodyBottomCenterPoint, BIG_RADIUS, fishAngle + 90);
        PointF bottomRight = calculatePoint(bodyBottomCenterPoint, BIG_RADIUS, fishAngle - 90);

        PointF controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130);
        mPath.reset();
        mPath.moveTo(topLeft.x, topLeft.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomLeft.x, bottomLeft.y);
        mPath.lineTo(bottomRight.x, bottomRight.y);
        mPath.quadTo(controlRight.x, controlRight.y, topRight.x, topRight.y);
        mPath.close();
        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private PointF makeSegment(Canvas canvas, PointF bodyBottomCenterPoint, float fishAngle,
                             float startPointLength, float firstRadius, float afterRadius, boolean hasBigCircle) {
        float segmentAngle;
        if (hasBigCircle) {
            segmentAngle = fishAngle + (float) Math.cos(Math.toRadians(currentValue * 1.5f * frequency)) * 15;     // 改变频率，必须让currentValue是360和1.2的公倍数，否则出现卡顿
        } else {
            segmentAngle = fishAngle + (float) Math.sin(Math.toRadians(currentValue * 1.5f * frequency) ) * 15;
        }
        PointF upperCenterPoint = calculatePoint(bodyBottomCenterPoint, startPointLength, segmentAngle - 180);
        PointF bottomLeftPoint = calculatePoint(bodyBottomCenterPoint, firstRadius, segmentAngle + 90);
        PointF bottomRightPoint = calculatePoint(bodyBottomCenterPoint, firstRadius, segmentAngle - 90);
        PointF upperLeftPoint = calculatePoint(upperCenterPoint, afterRadius, segmentAngle + 90);
        PointF upperRightPoint = calculatePoint(upperCenterPoint, afterRadius, segmentAngle - 90);

        if (hasBigCircle) {
            // 画大圆
            canvas.drawCircle(bodyBottomCenterPoint.x, bodyBottomCenterPoint.y, firstRadius, mPaint);
        }

        // 画小圆
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, afterRadius, mPaint);

        // 画梯形
        mPath.reset();
        mPath.moveTo(upperLeftPoint.x, upperLeftPoint.y);
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.lineTo(bottomLeftPoint.x, bottomLeftPoint.y);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        return upperCenterPoint;
    }

    private void makeTriangle(Canvas canvas, PointF startPoint, float centerLength, float edgeLength, float fishAngle) {
        PointF centerPoint = calculatePoint(startPoint, centerLength, fishAngle - 180);
        PointF leftPoint = calculatePoint(centerPoint, edgeLength, fishAngle + 90);
        PointF rightPoint = calculatePoint(centerPoint, edgeLength, fishAngle - 90);
        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void makeFins(Canvas canvas, PointF startPoint, float fishAngle, boolean isRight) {
        float controlAngle = 115;

        PointF endPoint = calculatePoint(startPoint, FIND_LENGTH, fishAngle - 180);
        PointF controlPoint;
        float fishControlAngle = isRight ? fishAngle - controlAngle : fishAngle + controlAngle;
        if (isSwim) {
            controlPoint = calculatePoint(startPoint, (float) (FIND_LENGTH * 1.8f - Math.abs(Math.sin(currentValue / 4) * FIND_LENGTH * 0.5f)), fishControlAngle);
        } else {
            controlPoint = calculatePoint(startPoint, FIND_LENGTH * 1.8f, fishControlAngle);
        }
        mPath.reset();
        mPath.moveTo(startPoint.x , startPoint.y);
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * 通过角度、边长可以计算出坐标
     */
    public PointF calculatePoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) (Math.cos(Math.toRadians(angle)) * length);
        float deltaY = (float) (Math.sin(Math.toRadians(angle  - 180)) * length);
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);

        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS); // 重心是手算出来的

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 3600f);
        valueAnimator.setDuration(5200);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);         // 重新开始
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);       // 重复的模式，重新开始
        valueAnimator.setInterpolator(new LinearInterpolator());    // 插值器
        valueAnimator.addUpdateListener(animation -> {
            currentValue = (float) animation.getAnimatedValue();
            invalidateSelf();
        });
        valueAnimator.start();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);     // 手算出来的
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * HEAD_RADIUS);     // 手算出来的
    }

    public PointF getMiddlePoint() {
        return middlePoint;
    }

    public PointF getHeaderPoint() {
        return headPoint;
    }

    public float getHEAD_RADIUS() {
        return HEAD_RADIUS;
    }

    public void setAngle(float angle) {
        this.fishMainAngle = angle;
    }

    public void startSwim() {
        isSwim = true;
    }

    public void stopSwim() {
        isSwim = false;
    }
}
