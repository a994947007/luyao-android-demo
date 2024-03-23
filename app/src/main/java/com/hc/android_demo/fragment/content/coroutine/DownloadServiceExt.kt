package com.hc.android_demo.fragment.content.coroutine

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.luyao.android.demo.download.core.DownloadCallback
import com.luyao.android.demo.download.download.DownloadService
import com.luyao.android.demo.download.process.DownloadCenter
import com.luyao.android.demo.download.process.DownloadDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.net.URL

fun DownloadService.downloadImageFlow(url: URL): Flow<Bitmap> {
    return flow {
        val bitmap = downloadImage(url)
            ?: throw IOException("Download image exception")
        emit(bitmap)
    }.flowOn(Dispatchers.IO)
}

fun DownloadService.downloadFileFlow(url: String): Flow<State> {
    return callbackFlow {
        val disposable = DownloadCenter.getInstance().download(url, object: DownloadCallback {
            override fun onStart() {
                Log.d("DownloadService", "download start")
            }

            override fun onDownload(percent: Float) {
                trySend(State.Progress(percent))
            }

            override fun onSuccess(url: String) {
                trySend(State.Success(url))
            }

            override fun onError(e: Throwable) {
                close(e)
            }
        })
        awaitClose {
            disposable.dispose()
        }
    }
    .flowOn(Dispatchers.IO)
}

sealed class State {
    data class Success(val url: String): State()
    data class Progress(val percent: Float): State()
}

class LifecycleDefault: LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun dispatchState(event: Lifecycle.Event) {
        lifecycleRegistry.handleLifecycleEvent(event)
    }
}