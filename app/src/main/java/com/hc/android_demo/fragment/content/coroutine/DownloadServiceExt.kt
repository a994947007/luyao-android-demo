package com.hc.android_demo.fragment.content.coroutine

import android.graphics.Bitmap
import com.luyao.android.demo.download.download.DownloadService
import com.luyao.android.demo.download.download.LuDownload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.net.URL

fun DownloadService.downloadImageFlow(url: URL): Flow<Bitmap> {
    return flow {
        val bitmap = LuDownload.getInstance().downloadService.downloadImage(url)
            ?: throw IOException("Download image exception")
        emit(bitmap)
    }.flowOn(Dispatchers.IO)
}