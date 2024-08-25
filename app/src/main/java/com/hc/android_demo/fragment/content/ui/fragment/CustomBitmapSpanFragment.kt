package com.hc.android_demo.fragment.content.ui.fragment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import com.hc.android_demo.R
import com.hc.base.fragment.LuFragment
import com.hc.util.ViewUtils
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import java.util.concurrent.locks.ReentrantLock


@ARouter(
    path = FragmentConstants.SURFACE_VIEW_DRAW_TEXT_VIEW,
    group = FragmentConstants.CUSTOM_VIEW
)
class CustomBitmapSpanFragment: LuFragment() {

    private lateinit var surfaceView: SurfaceView
    private lateinit var holder: SurfaceHolder
    private var th: Thread? = null
    private var isDraw = false
    private val LOCK = ReentrantLock()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_draw_text_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        surfaceView = view.findViewById(R.id.surfaceView)
        surfaceView.setZOrderOnTop(true)
        surfaceView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    private val globalLayoutListener by lazy {
        OnGlobalLayoutListener {
            holder = surfaceView.holder
            holder.setFormat(PixelFormat.TRANSLUCENT)
            th = Thread(runnable)
            th?.isDaemon = true
            th?.start()
        }
    }

    private val runnable by lazy {
        Runnable {
            while (th?.isInterrupted == false) {
                var c: Canvas? = null
                try {
                    if (!isDraw) {
                        c = holder.lockCanvas()
                        synchronized(holder) {
                            c?.also {
                                drawTextView(it)
                                it.translate(0f, 200f)
                                drawStateLayout(it)
                            }
                        }
                    }
                    th?.interrupt()
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c)
                    }
                }
            }
        }
    }

    private fun drawTextView(canvas: Canvas) {
        val textView = TextView(context)
        textView.text = "哈哈哈"
        textView.setTextColor(Color.BLUE)
        // 采用先画到bitmap上，然后再画到surface
        val tempBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val cv = Canvas(tempBitmap)
        textView.layout(0, 0, 200, 200)
        textView.draw(cv)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        // 绘制Bitmap
        canvas.drawBitmap(tempBitmap, 0f, 0f, null) // 参数为(Bitmap, x, y, Paint)
    }

    private fun drawStateLayout(canvas: Canvas) {
        val currentBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val paint = TextPaint()
        paint.setColor(ViewUtils.getColor(R.color.colorPrimary))
        paint.isAntiAlias = true
        paint.textSize = ViewUtils.dp2px(15f).toFloat()
        val staticLayout = StaticLayout("嘿嘿嘿", paint, 200, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
        val cv = Canvas(currentBitmap)
        staticLayout.draw(cv)
        canvas.drawBitmap(currentBitmap, 0f, 0f, null)
    }

    private fun stopThread() {
        try {
            th?.interrupt()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        surfaceView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        stopThread()
    }
}