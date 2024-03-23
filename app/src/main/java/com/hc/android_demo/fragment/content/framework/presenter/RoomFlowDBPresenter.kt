package com.hc.android_demo.fragment.content.framework.presenter

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.hc.android_demo.R
import com.hc.android_demo.fragment.content.framework.db.AppDataBase
import com.hc.android_demo.fragment.content.framework.db.Comment
import com.hc.android_demo.fragment.content.framework.fragment.RoomFlowTestFragment
import com.hc.support.mvps.Presenter
import com.hc.util.ToastUtils
import com.jny.android.demo.base_util.TextUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class RoomFlowDBPresenter: Presenter() {
    private lateinit var mEditText: EditText
    private lateinit var mInsertBtn: Button
    private lateinit var mUpdateBtn: Button
    private lateinit var mDeleteBtn: Button
    private lateinit var mQueryBtn: Button
    private lateinit var fragment: RoomFlowTestFragment

    override fun onCreate() {
        super.onCreate()
    }

    override fun doBindView(rootView: View) {
        super.doBindView(rootView)
        mEditText = rootView.findViewById(R.id.edit_text)
        mInsertBtn = rootView.findViewById(R.id.insert_btn)
        mUpdateBtn = rootView.findViewById(R.id.update_btn)
        mDeleteBtn = rootView.findViewById(R.id.delete_btn)
        mQueryBtn = rootView.findViewById(R.id.query_btn)
    }

    override fun doInject() {
        fragment = inject(RoomFlowTestFragment::class.java)
    }

    override fun onBind() {
        super.onBind()
        mInsertBtn.setOnClickListener { v: View? ->
            val text = mEditText.text.toString()
            if (TextUtils.isEmpty(text)) {
                return@setOnClickListener
            }
            fragment.lifecycleScope.launch {
                val comment = Comment(0, text)
                fragment.lifecycleScope.launch {
                    AppDataBase.getInstance().commentDao().insert(comment)
                }
            }
        }
        mQueryBtn.setOnClickListener { v: View? ->
            fragment.lifecycleScope.launch {
                val text = mEditText.text.toString()
                if (!TextUtils.isEmpty(text)) {
                    AppDataBase.getInstance().commentDao().findCommentByText(text)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        ToastUtils.show(text)
                    }
                }
            }
        }
    }
}