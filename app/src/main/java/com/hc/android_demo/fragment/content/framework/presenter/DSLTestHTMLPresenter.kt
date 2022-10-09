package com.hc.android_demo.fragment.content.framework.presenter

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hc.android_demo.R
import com.hc.base.util.KtPresenter
import java.lang.StringBuilder

class DSLTestHTMLPresenter: KtPresenter() {

    private lateinit var mHtmlButton: Button
    private lateinit var mContentTV: TextView

    override fun doBindView(rootView: View) {
        super.doBindView(rootView)
        mHtmlButton = rootView.findViewById(R.id.html_dsl_btn)
        mContentTV = rootView.findViewById(R.id.content_tv)
    }

    interface Element {
        fun render(content:StringBuilder, indent: String)
    }

    abstract class Tag(val name: String): Element {
        val children = arrayListOf<Element>()
        val attributes = hashMapOf<String, String?>()

        fun <T: Element> initTag(lambda: T.() -> Unit, tag: T): T {
            lambda(tag)
            children.add(tag)
            return tag
        }

        override fun render(content: StringBuilder, indent: String) {
            content.append("$indent<$name${renderAttributes()}>\n")
            for (child in children) {
                child.render(content, indent + "\t")
            }
            content.append("$indent</$name>\n")
        }

        private fun renderAttributes(): String {
            val builder = StringBuilder()
            for (key in attributes.keys) {
                builder.append(" $key=\"${attributes[key]}\"")
            }
            return builder.toString()
        }

        operator fun String.unaryMinus() {
            children.add(TextElement(this))
        }

        fun setAttribute(attribute: String, attributeValue: String) {
            attributes[attribute] = attributeValue
        }

        fun getAttribute(attribute: String) = attributes[attribute]
    }

    class TextElement(val name: String): Element {
        override fun render(content: StringBuilder, indent: String) {
            content.append("$indent$name\n")
        }
    }

    class Style: Tag("style")

    class Script: Tag("script")

    class A: Tag("a")

    class H1: Tag("h1")

    class H2: Tag("h2")

    class Div: Tag("div") {
        fun div(lambda: Div.() -> Unit) = initTag(lambda, Div())
    }

    class Header: Tag("header") {
        fun style(type: String = com.jny.android.demo.base_util.TextUtils.emptyString(),
                  lambda: Style.() -> Unit) {
            val style = Style()
            style.setAttribute("type", type)
            initTag(lambda, style)
        }

        fun script(type: String = com.jny.android.demo.base_util.TextUtils.emptyString(),
                   lambda: Script.() -> Unit) {
            val script = Script()
            script.setAttribute("type", type)
            initTag(lambda, script)
        }
    }

    class Body: Tag("body") {
        fun a(href: String = com.jny.android.demo.base_util.TextUtils.emptyString(),
              lambda: A.() -> Unit) {
            val a = A()
            a.setAttribute("href", href)
            initTag(lambda, a)
        }

        fun h1(lambda: H1.() -> Unit) = initTag(lambda, H1())

        fun h2(lambda: H2.() -> Unit) = initTag(lambda, H2())

        fun div(lambda: Div.() -> Unit) = initTag(lambda, Div())
    }

    class Html: Tag("html") {
        fun header(lambda: Header.() -> Unit) = initTag(lambda, Header())

        fun body(lambda: Body.() -> Unit) = initTag(lambda, Body())

        fun build():String {
            val builder = StringBuilder()
            render(builder, com.jny.android.demo.base_util.TextUtils.emptyString())
            return builder.toString()
        }
    }

    fun html(lambda: Html.() -> Unit): Html {
        val html = Html()
        lambda(html)
        return html
    }

    override fun onBind() {
        super.onBind()
        val html = html {
            header {
                style {

                }

                script {

                }
            }

            body {
                h1 {
                    -"主标题"
                }

                h2 {
                    -"副标题"
                }

                a(href = "www.baidu.com") {
                    -"点击跳转"
                }

                div {
                    div {
                       -"标签嵌套"
                    }
                }
            }
        }

        val htmlString = html.build()
        mHtmlButton.setOnClickListener {
            mContentTV.text = htmlString
        }
    }

}