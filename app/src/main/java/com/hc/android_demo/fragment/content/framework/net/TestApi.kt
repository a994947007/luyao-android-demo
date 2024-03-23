package com.hc.android_demo.fragment.content.framework.net

import com.hc.android_demo.fragment.content.mvp.model.UserListResponse
import com.hc.support.singleton.annotation.ApiService
import retrofit2.http.GET

@ApiService
interface TestApi {

    @GET("/api/users?page=2")
    suspend fun getUsers(): UserListResponse

}