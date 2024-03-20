package com.hc.android_demo.fragment.content.coroutine

import android.graphics.Bitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.luyao.android.demo.download.core.DownloadCallback
import com.luyao.android.demo.download.download.DownloadService
import com.luyao.android.demo.download.download.LuDownload
import com.luyao.android.demo.download.process.DownloadCenter
import com.luyao.android.demo.download.process.DownloadDisposable
import kotlinx.coroutines.*
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
    var disposable: DownloadDisposable?= null
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    return flow {
        disposable = DownloadCenter.getInstance().download(url, object: DownloadCallback {
            override fun onStart() { }

            override fun onDownload(percent: Float) {
                scope.launch {
                    emit(State.Progress(percent))
                }
            }

            override fun onSuccess(url: String) {
                scope.launch {
                    emit(State.Success(url))
                }
            }

            override fun onError(e: Throwable) {
                throw e
            }

        })
    }
    .flowOn(Dispatchers.IO)
    .catch {
        disposable?.dispose()
        scope.cancel()
    }
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