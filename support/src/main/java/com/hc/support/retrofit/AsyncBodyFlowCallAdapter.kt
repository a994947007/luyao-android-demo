package com.hc.support.retrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import java.lang.reflect.Type
import kotlin.coroutines.resumeWithException

/**
 * 将Call<ResponseBody>、Call<Bean>转化为Flow<Response>
 */
class AsyncBodyFlowCallAdapter<R>(private val responseBodyType: R): CallAdapter<R, Flow<R>> {
    override fun responseType(): Type {
        return responseBodyType as Type
    }

    override fun adapt(call: Call<R>): Flow<R> {
        return bodyFlow(call)
    }

    private fun bodyFlow(call: Call<R>): Flow<R> {
        // callbackFlow通常用于发送多个数据，这里只有一个，所以用suspendCancellableCoroutine
        return flow {
            suspendCancellableCoroutine<R> { continuation ->
                continuation.invokeOnCancellation {
                    call.cancel()
                }
                try {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (response.isSuccessful) {
                                continuation.resume(response.body()!!, null)
                            } else {
                                continuation.resumeWithException(HttpException(response))
                            }
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }.let {
                emit(it)
            }
        }
    }
}