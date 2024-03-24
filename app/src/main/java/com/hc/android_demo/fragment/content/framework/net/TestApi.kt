package com.hc.android_demo.fragment.content.framework.net

import com.hc.android_demo.fragment.content.mvp.model.Response
import com.hc.android_demo.fragment.content.mvp.model.UserListResponse
import com.hc.android_demo.fragment.content.mvp.model.UserModel
import com.hc.support.singleton.annotation.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@ApiService
interface TestApi {

    @GET("/api/users?page=2")
    suspend fun getUsers(): UserListResponse

    @GET("/api/users/2")
    fun getUser(): Flow<Response<UserModel>>

}