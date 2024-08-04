package com.hc.android_demo.fragment.content.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hc.android_demo.R
import com.hc.base.fragment.LuFragment
import com.hc.my_views.textview.CustomTextView
import com.hc.my_views.textview.span.ColorSpan
import com.hc.my_views.textview.span.CustomSpannableStringBuilder
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants


@ARouter(
    path = FragmentConstants.CUSTOM_SPAN_FRAGMENT,
    group = FragmentConstants.CUSTOM_VIEW
)
class CustomSpanFragment : LuFragment() {

    private lateinit var customSpanTextView: TextView
    private lateinit var customTextView: CustomTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_span_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customSpanTextView = view.findViewById(R.id.text_view_custom_span)
        customTextView = view.findViewById(R.id.text_view_custom_text_view)
        customSpanTextView()
        customTextView()
    }

    /**
     * Spannable.SPAN_INCLUSIVE_INCLUSIVE 前后都包括
     * Spannable.SPAN_INCLUSIVE_EXCLUSIVE 前包括，后不包括
     * Spannable.SPAN_EXCLUSIVE_INCLUSIVE 前不包括，后包括
     * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE 前后都不包括
     * 这个属性只对Builder.insert()方法有效，例如前面调用了 ForegroundColorSpan(Color.RED),
     *             1, // start
     *             2, // end
     *             Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
     *  那么，档Builder.insert(1, 'A')，则A会被渲染成红色
     *
     */
    private fun customSpanTextView() {
        val text = SpannableStringBuilder("Hello world!")
        text.setSpan(
            ForegroundColorSpan(Color.RED),
            1, // start
            2, // end
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        text.insert(1, "a")
        text.insert(3, "b")
        text.insert(0, "c")
        text.insert(4, "d")
        // 上面4个字符都会被染成红色，注意，及时设置的是4的位置，由于此时的4也是在之前e的后边，会被INCLUSIVE包括，也会被染成红色

        text.setSpan(
            StyleSpan(Typeface.BOLD),
            4,
            8,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        text.setSpan(
            UnderlineSpan(),
            4,
            8,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        customSpanTextView.text = text
    }

    private fun customTextView() {
        val spannableStringBuilder = CustomSpannableStringBuilder()
        spannableStringBuilder.appendText("abcdef");
        spannableStringBuilder.addSpan(ColorSpan(Color.RED), 1, 2)
        customTextView.setText(spannableStringBuilder)
    }
}