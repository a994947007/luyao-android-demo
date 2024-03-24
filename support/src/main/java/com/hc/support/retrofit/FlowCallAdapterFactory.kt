package com.hc.support.retrofit

import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory: CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) return null
        // 强制需要带泛型参数
        if (returnType !is ParameterizedType) {
            throw IllegalArgumentException("this flow type must as Flow<*>")
        }
        val flowType = getParameterUpperBound(0, returnType)
        // 得到Flow<T> T的真实类型
        val rawFlowType = getRawType(flowType)

        return if (rawFlowType == Response::class.java) {
            // Flow<T> T必须是Response<*>模式
            if (flowType !is ParameterizedType) {
                throw IllegalArgumentException("this flow type must as Flow<Response<*>>")
            }
            val responseBodyType = getParameterUpperBound(0 ,flowType)
            AsyncResponseFlowAdapter(responseBodyType)
        } else {
            AsyncBodyFlowCallAdapter(flowType)
        }
    }

    companion object {
        @JvmStatic
        fun create()  = FlowCallAdapterFactory()
    }
}