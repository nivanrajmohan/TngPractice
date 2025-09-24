package com.raj.tngpractice.feature.user.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface UserAPI {

    @GET("users")
    suspend fun fetchAllUsers() : Response<String>

}