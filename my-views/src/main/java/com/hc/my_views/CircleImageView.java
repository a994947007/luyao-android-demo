package com.hc.my_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.hc.util.ViewUtils;

/**
 * 参考：https://blog.csdn.net/bobo_zai/article/details/104653340
 */
public class CircleImageView extends AppCompatImageView {
    private RectF rectF;
    private final Paint imagePaint = new Paint();
    private Paint borderPaint = new Paint();
    private int mRadius;
    private int mBorder;
    protected int mBorderColor = Color.BLUE;
    private Matrix mMatrix = new Matrix();

    public CircleImageView(@NonNull Context context) {
        this(context, null);
    }

    public CircleImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(attrs);
        init();
    }

    private void init() {
        // 背景圆画笔
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setDither(true);
        borderPaint.setColor(mBorderColor);
        // 只画圆弧
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(mBorder);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//禁用硬加速

        imagePaint.setAntiAlias(true);
        imagePaint.setDither(true);
    }

    private void loadAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mBorder = ViewUtils.dp2px(typedArray.getDimension(R.styleable.CircleImageView_border, 0));
        mBorderColor = typedArray.getColor(R.styleable.CircleImageView_borderColor, Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rectF == null) {
            rectF = new RectF(0, 0, getWidth(), getBottom());
            mRadius = getWidth() / 2 - mBorder;
        }
        drawImage(canvas);
        if (mBorder > 0) {
            drawBorder(canvas);
        }
    }

    private void drawImage(Canvas canvas) {
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), imagePaint);
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            //获取到Bitmap的宽高
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            //获取到ImageView的宽高
            //CLAMP, MIRROR, REPEAT
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mMatrix.reset();
            float minScale = Math.min(getWidth() / (float) bitmapWidth, getHeight() / (float) bitmapHeight);
            mMatrix.setScale(minScale, minScale);//将Bitmap保持比列根据ImageView的大小进行缩放
            bitmapShader.setLocalMatrix(mMatrix); //将矩阵变化设置到BitmapShader,其实就是作用到Bitmap
            imagePaint.setShader(bitmapShader);
            canvas.drawCircle(getWidth() / 2.0f, getWidth() / 2.0f, mRadius, imagePaint);
        } else {
            super.onDraw(canvas);
        }
        canvas.restoreToCount(layerId);
    }

    private void drawBorder(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mRadius + mBorder / 2.0f, borderPaint);
        canvas.restore();
    }
}
