package com.example.sqlitedb.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/External_Api/Mobile_Api/Invoke")
    fun getApiResponse(@Body requestBody: ApiRequestBody): Call<ApiResponse>
}