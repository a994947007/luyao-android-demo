package com.hc.android_demo.fragment.content.ui.fragment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
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
import android.widget.TextView
import com.hc.android_demo.R
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants


@ARouter(
    path = FragmentConstants.SURFACE_VIEW_DRAW_TEXT_VIEW,
    group = FragmentConstants.CUSTOM_VIEW
)
class CustomBitmapSpanFragment: LuFragment() {

    private lateinit var surfaceView: SurfaceView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_draw_text_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        surfaceView = view.findViewById(R.id.surfaceView)
        val textView = TextView(context)
        textView.text = "哈哈哈"
        textView.setTextColor(Color.BLUE)
        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        textView.layout(0, 0, 200, 200)
        textView.draw(canvas)

        bitmap2 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val staticLayout = StaticLayout("嘿嘿嘿", textView.paint, 200, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
        val canvas2 = Canvas(bitmap2)
        staticLayout.draw(canvas2)

        surfaceView.setZOrderOnTop(true)
        surfaceView.viewTreeObserver.addOnGlobalLayoutListener {
            holder = surfaceView.holder
            holder.setFormat(PixelFormat.TRANSLUCENT)
            running = true
            th = Thread(runnable)
            th!!.start()
        }

    }

    private lateinit var holder: SurfaceHolder
    private var th: Thread? = null
    private lateinit var bitmap: Bitmap
    private var running = false
    private lateinit var bitmap2: Bitmap

    private val runnable by lazy {
        Runnable {
            while (running) {
                var c: Canvas? = null
                try {
                    c = holder.lockCanvas()
                    synchronized(holder) {
                        if (c != null) {
                            c?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                            // 绘制Bitmap
                            c?.drawBitmap(bitmap, 0f, 0f, null) // 参数为(Bitmap, x, y, Paint)

                            c?.translate(0f, 200f)
                            c?.drawBitmap(bitmap2, 0f, 0f, null)
                        }
                    }
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c)
                    }
                }
            }
        }
    }

    fun stopThread() {
        running = false
        try {
            th!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopThread()
    }
}