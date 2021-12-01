package com.hc.my_views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

import com.hc.util.BitmapUtils;
import com.hc.util.ViewUtils;

public class PhotoView extends View {

    private static final float IMAGE_WIDTH = ViewUtils.dp2px(300F);
    private Bitmap bitmap;
    private Paint paint;
    private float originalOffsetX;
    private float originalOffsetY;
    private float minScale;
    private float maxScale;
    private float currentScale;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private int offsetX;    // 距离图片中心点x轴方向上的偏移值
    private int offsetY;    // 距离图片中心点y轴方向上的偏移值
    private boolean isLarge = false;
    private OverScroller overScroller;
    private static final int MAX_OVER_SCROLL_SIZE = 100;
    private boolean isFlingging = false;

    private float OVER_SCALE_FACTOR = 1.5f;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        loadAttrs(context, attrs);
        gestureDetector = new GestureDetector(context, new PhotoViewGestureDetector());
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());
        overScroller = new OverScroller(context);
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoView);
        int bitmapResId = typedArray.getResourceId(R.styleable.PhotoView_android_src, 0);
        if (bitmapResId != 0) {
            bitmap = BitmapUtils.getBitmap(context.getResources(), bitmapResId, (int) IMAGE_WIDTH);
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.scale缩放
        canvas.translate(offsetX, offsetY);
        canvas.scale(currentScale, currentScale, getWidth()/2f, getHeight()/2f);
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentNeedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentNeedHeight = MeasureSpec.getSize(heightMeasureSpec);
        parentNeedHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ?
                parentNeedHeight : Math.max(parentNeedHeight, (int) ((float)bitmap.getHeight() * parentNeedWidth / bitmap.getWidth()));
        setMeasuredDimension(parentNeedWidth, parentNeedHeight);
    }

    // 在onDraw之前会被调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalOffsetX = (getWidth() - bitmap.getWidth()) / 2f;
        originalOffsetY = (getHeight() - bitmap.getHeight()) / 2f;

        // 计算最小缩放和最大缩放
        if ((float)bitmap.getWidth() / bitmap.getHeight() > (float)getWidth() / getHeight()) {
            minScale = (float)getWidth() / bitmap.getWidth();
            maxScale = (float)getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            minScale = (float)getHeight() / bitmap.getHeight();
            maxScale = (float)getWidth() / bitmap.getWidth()* OVER_SCALE_FACTOR;
        }
        currentScale = minScale;
    }

    private void fixOffset() {
        offsetX = fixOffsetX(offsetX, currentScale);
        offsetY = fixOffsetY(offsetY, currentScale);
    }

    private int fixOffsetX(int x, float scale) {
        int minOffsetX = -(int) ((bitmap.getWidth() * scale - getWidth()) / 2);
        int maxOffsetX = -minOffsetX;
        if (maxOffsetX > 0) {   // 长图时，缩到最小，minOffSetX是负数了
            x = MathUtils.clamp(x, minOffsetX, maxOffsetX);
        } else {
            x = 0;
        }
        return x;
    }

    private int fixOffsetY(int y, float scale) {
        int minOffsetY = -(int) ((bitmap.getHeight() * scale - ((View)getParent()).getHeight()) / 2);
        int maxOffsetY = -minOffsetY;
        if (isLongImage()) {
            maxOffsetY = (int) ((bitmap.getHeight() * scale - getHeight()) / 2);
            minOffsetY = -(int) (bitmap.getHeight() * scale - ((View)getParent()).getHeight()) + maxOffsetY;
            y = MathUtils.clamp(y, minOffsetY, maxOffsetY);
        } else {
            if (maxOffsetY > 0) {
                y = MathUtils.clamp(y, minOffsetY, maxOffsetY);
            } else {
                y = 0;
            }
        }
        return y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()) {
            result = gestureDetector.onTouchEvent(event);
        }
        return result;
    }

    protected boolean isLongImage() {
        return (float)bitmap.getHeight() / bitmap.getWidth() > (float) ((View)getParent()).getHeight() / ((View)getParent()).getHeight();
    }

    protected class PhotoViewGestureDetector extends GestureDetector.SimpleOnGestureListener {
        /**
         * 单击触发，或者双击的第一次触发， 如果是双击的第二次或者长按不触发
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        /**
         * 长按，实现原理，放入Handler延时队列中，如果Down事件超过N秒执行，up、cancel事件会移除，这就保证了Long事件能够顺利执行
         * @param e
         */
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        /**
         * move事件的时候调用，同时会移除long事件和show_press事件还有tap事件
         * @param e1 手指按下的event
         * @param e2 当前手指位置的event
         * @param distanceX 旧位置 - 新位置
         * @param distanceY 旧位置 - 新位置
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isLarge || isLongImage()) {
                offsetX -= distanceX;
                offsetY -= distanceY;
                fixOffset();
                invalidate();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        /**
         * up事件的时候调用，看速度是否大于阈值，大于则触发
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (isLarge || isLongImage()) {
                int minOffsetX = -(int) ((bitmap.getWidth() * currentScale - getWidth()) / 2);
                int maxOffsetX = -minOffsetX;
                int minOffsetY = -(int) ((bitmap.getHeight() * currentScale - getHeight()) / 2);
                int maxOffsetY = -minOffsetY;
                if (maxOffsetY < 0) {
                    maxOffsetY = 0;
                }
                if (minOffsetY > 0) {
                    minOffsetY = 0;
                }
                if (isLongImage()) {
                    maxOffsetY = (int) ((bitmap.getHeight() * currentScale - getHeight()) / 2);
                    minOffsetY = -(int) (bitmap.getHeight() * currentScale - ((View)getParent()).getHeight()) + maxOffsetY;
                    overScroller.fling(offsetX, offsetY, (int)velocityX, (int)velocityY,
                            minOffsetX, maxOffsetX, minOffsetY, maxOffsetY, 0, MAX_OVER_SCROLL_SIZE);
                } else {
                    overScroller.fling(offsetX, offsetY, (int)velocityX, (int)velocityY,
                            minOffsetX, maxOffsetX, minOffsetY, maxOffsetY, MAX_OVER_SCROLL_SIZE, MAX_OVER_SCROLL_SIZE);
                }
                isFlingging = true;
                ViewCompat.postOnAnimation(PhotoView.this, new FlingRunnable());
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        /**
         * 用于实现类似于水波纹效果等事件，延时100ms触发
         */
        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        /**
         * down事件一定会触发，需要返回true，表示接收事件
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * 双击触发，通过发送一个延时event到handler队列中，如果事件到了还未进行第二次down，则remove不触发，
         * 如果在还未执行前又收到down，则认为是双击，为了防止抖动，这里还需要第二次down与第一次down大于某个时间阈值才能完成双击事件的触发（40ms-300ms）
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isLarge = !isLarge;
            if (isLarge) {
                // 防撞处理
                targetOffsetX = (int) ((e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2f) * maxScale / currentScale);
                targetOffsetY = (int) ((e.getY() - ((View)getParent()).getHeight() / 2f) - (e.getY() - ((View)getParent()).getHeight() / 2f) * maxScale / currentScale) + offsetY;
                targetOffsetY = fixOffsetY(targetOffsetY, minScale + (maxScale - minScale) * 1f / 2f);
                targetScale = maxScale;
            } else {
                targetOffsetX = 0;
                targetOffsetY = 0;
                if (isLongImage()) {
                    // 防撞处理
                    targetOffsetY = (int) (offsetY * minScale / currentScale);
                    targetOffsetY = fixOffsetY(targetOffsetY, minScale);
                }
                targetScale = minScale;
            }
            getScaleAnimator().start();
            return super.onDoubleTap(e);
        }

        /**
         * 与onDoubleTap的区别是：双击之后，该回调在down、move、up事件时都触发
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        /**
         * 单击，300ms内抬起触发，300ms后不是长按和双击触发
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    }

    private ValueAnimator scaleAnimator;
    private int targetOffsetX;
    private int targetOffsetY;
    private float targetScale;

    protected ValueAnimator getScaleAnimator() {
        float scaleLong = targetScale - currentScale;
        float initScale = currentScale;
        int offsetXLong = targetOffsetX - offsetX;
        int offsetYLong = targetOffsetY - offsetY;
        int initOffsetX = offsetX;
        int initOffsetY = offsetY;
        if (scaleAnimator == null) {
            scaleAnimator = ValueAnimator.ofFloat(0, 1);
        }

        ValueAnimator.AnimatorUpdateListener scaleUpdateListener = animation -> {
            float ratio = (float) animation.getAnimatedValue();
            currentScale = initScale + scaleLong * ratio;
            offsetX = (int) (initOffsetX + offsetXLong * ratio);
            offsetY = (int) (initOffsetY + offsetYLong * ratio);
            fixOffset();
            invalidate();
        };

        scaleAnimator.addUpdateListener(scaleUpdateListener);
        scaleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scaleAnimator.removeUpdateListener(scaleUpdateListener);
                scaleAnimator.removeListener(this);
            }
        });
        return scaleAnimator;
    }

    class FlingRunnable implements Runnable {
        @Override
        public void run() {
            if (overScroller.computeScrollOffset()) {
                offsetX = overScroller.getCurrX();
                offsetY = overScroller.getCurrY();
                invalidate();
                ViewCompat.postOnAnimation(PhotoView.this, this);
            } else {
                isFlingging = false;
            }
        }
    }

    class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float initScale = 0;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (isFlingging) {
                return false;
            }
            currentScale = initScale * detector.getScaleFactor();
            currentScale = MathUtils.clamp(currentScale, minScale, maxScale);
            if (currentScale > minScale) {
                isLarge = true;
            } else {
                isLarge = false;
            }
            fixOffset();
            invalidate();
            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initScale = currentScale;
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }
}
