package com.hc.support.retrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 */
class AsyncResponseFlowAdapter<R>(private val responseBodyType: R) : CallAdapter<R, Flow<Response<R>>> {
    override fun responseType(): Type {
        return responseBodyType as Type
    }

    override fun adapt(call: Call<R>): Flow<Response<R>> {
        return responseFlow(call)
    }

    private fun responseFlow(call: Call<R>): Flow<Response<R>> {
        return flow {
            suspendCancellableCoroutine<Response<R>> { continuation ->
                continuation.invokeOnCancellation {
                    call.cancel()
                }

                try {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            continuation.resume(response)
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                    })
                } catch (e : Exception) {
                    continuation.resumeWithException(e)
                }
            }.let {
                emit(it)
            }
        }
    }
}