package com.hc.android_demo.fragment.content.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hc.android_demo.R
import com.hc.base.fragment.LuFragment
import com.jny.android.demo.arouter_annotations.ARouter
import com.jny.common.fragment.FragmentConstants
import java.text.BreakIterator

@ARouter(
    path = FragmentConstants.EMOJI_TEST_FRAGMENT,
    group = FragmentConstants.CUSTOM_VIEW
)
class EmojiTestFragment: LuFragment() {

    private lateinit var messageTV: TextView
    private val wordIterator = BreakIterator.getWordInstance();
    private val lineIterator = BreakIterator.getLineInstance();
    private val sentenceIterator = BreakIterator.getSentenceInstance();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_emoji_test_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageTV = view.findViewById(R.id.message)
        view.findViewById<View>(R.id.emoji_sub).setOnClickListener {
            subEmojis()
        }
    }

    private fun subEmojis() : String {
        val text = "\uD83D\uDC40哈哈哈\uD83D\uDE0D\uD83D\uDE0D\uD83D\uDC40哈哈哈"
        wordIterator.setText(text)
        val first = wordIterator.first()
        val next = wordIterator.next()
        messageTV.text = "first: $first , next:$next, text:${text.substring(first, next)}"
        return text
    }

}