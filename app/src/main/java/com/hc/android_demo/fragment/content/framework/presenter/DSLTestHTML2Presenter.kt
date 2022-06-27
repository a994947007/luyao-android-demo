package com.hc.android_demo.fragment.content.framework.presenter

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hc.android_demo.R
import com.hc.support.mvps.Presenter

class DSLTestHTML2Presenter: Presenter() {
    private lateinit var mHtmlButton: Button
    private lateinit var mContentTV: TextView

    interface Node {
        fun create(): String
    }

    open class BlockNode(val name: String): Node {
        val children = arrayListOf<Node>()
        val properties = hashMapOf<String, Any>()

        override fun create(): String {
            var text = ""
            text += "<$name${createProperties()}>"
            for (child in children) {
                text += child.create();
            }
            text += "</$name>"
            return text
        }
        
        fun createProperties():String {
            var text = ""
            for (key in properties.keys) {
                text += " $key=\"${properties[key]}\""
            }
            return text;
        }

        operator fun String.invoke(action: BlockNode.() -> Unit) {
            val node = BlockNode(this)
            action(node)
            children.add(node)
        }

        operator fun String.invoke(any: Any) {
            properties[this] = any
        }

        operator fun String.unaryPlus() {
            val textNode = TextNode(this)
            children.add(textNode)
        }
    }

    class TextNode(val text: String): Node {
        override fun create() = text
    }

    class Html: BlockNode("html") {
        fun header(lambda: BlockNode.() -> Unit) {
            val header = BlockNode("header")
            lambda(header)
            children.add(header)
        }

        fun body(lambda: BlockNode.() -> Unit) {
            val body = BlockNode("body")
            lambda(body)
            children.add(body)
        }
    }

    fun html(lambda: Html.() -> Unit) : Html {
        val html = Html()
        lambda(html)
        return html
    }

    override fun doBindView(rootView: View) {
        super.doBindView(rootView)
        mHtmlButton = rootView.findViewById(R.id.html_dsl_btn2)
        mContentTV = rootView.findViewById(R.id.content_tv)

        val html = html {
            header {
                "meta" {
                    "charset"("UTF-8")
                }
            }

            body {
                "div" {
                    "div" {
                        "h1" {
                            +"哈哈哈"
                        }
                    }
                }
            }
        }
        val htmlString = html.create()
        mHtmlButton.setOnClickListener {
            mContentTV.text = htmlString
        }
    }
}