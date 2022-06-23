package com.hc.android_demo.fragment.content.framework.presenter

import android.view.View
import android.widget.Button
import com.hc.android_demo.R
import com.hc.base.util.KtPresenter
import com.hc.util.ToastUtils

class DSLTestPresenter: KtPresenter() {

    private lateinit var mPersonDslTestBtn: Button

    override fun doBindView(rootView: View) {
        super.doBindView(rootView)
        mPersonDslTestBtn = rootView.findViewById(R.id.person_dsl_btn)
        mPersonDslTestBtn.setOnClickListener {
            val p = person {
                st = student {
                    name = "123"
                    setClass(1)
                }

                te = teacher {
                    name = "456"
                    setTitle("语文老师")
                }

                // 无返回值
                worker {
                    name = "789"
                    otherName = "0"
                }
            }
            log(p)
        }
    }

    fun log(person: DSLTestPresenter.Person) {
        ToastUtils.show("studentName:" + person.st?.name + ", studentNo:" + person.st?.no + ",,,")
    }

    class Student: Person() {
        var no = 1001
        private var mClassNo = 1
        fun setClass(classNo: Int) {
            mClassNo = classNo
        }
        fun getClass(): Int {
            return mClassNo
        }
    }

    class Teacher: Person() {
        private var mTitle = ""
        fun setTitle(title: String) {
            mTitle = title
        }
        fun getTitle(): String {
            return mTitle
        }
    }

    class Worker: Person() {
        var otherName = ""
    }

    open class Person {
        var name: String = ""
        var st: Student? = null
        var te: Teacher? = null
        var wo: Worker? = null

        fun student(lambda: Student.() -> Unit): Student {
            val student = Student();
            lambda(student)
            return student
        }

        fun teacher(lambda: Teacher.() -> Unit): Teacher {
            val teacher = Teacher();
            lambda(teacher)
            return teacher;
        }

        fun worker(lambda: Worker.() -> Unit) {
            val worker = Worker();
            lambda(worker)
            wo = worker
        }
    }

    fun person(lambda: Person.() -> Unit): Person {
        val p = Person()
        lambda(p)
        return p
    }
}