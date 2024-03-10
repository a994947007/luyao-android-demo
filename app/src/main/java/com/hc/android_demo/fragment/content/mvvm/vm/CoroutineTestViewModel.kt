package com.hc.android_demo.fragment.content.mvvm.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoroutineTestViewModel: ViewModel() {

    private val usernameLiveData = MutableLiveData<String>()

    fun refreshUserNameText() {
        viewModelScope.launch {
            Log.d(TAG, "作用域：$this，线程名: ${Thread.currentThread().name}")
            val username = getUserName()
            usernameLiveData.value = username
        }
    }

    private suspend fun getUserName(): String = withContext(Dispatchers.IO) {
        delay(500)
        "ViewModelScope学习"
    }

    companion object {
        const val TAG = "CoroutineTestViewModel"
    }

}