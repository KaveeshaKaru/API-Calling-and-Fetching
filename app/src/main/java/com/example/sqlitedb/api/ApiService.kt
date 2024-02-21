package com.example.sqlitedb.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/External_Api/Mobile_Api/Invoke")
    suspend fun invokeApi(@Body requestBody: ApiRequestBody): Response<ApiResponse>

    @POST("api/External_Api/Mobile_Api/Invoke")
    suspend fun invoiceData(@Body requestBody: ApiRequestBody2): Response<ApiResponseInvoice>

    @POST("/api/External_Api/Mobile_Api/Invoke")
    suspend fun getAlbums(@Body requestBody: ApiRequestBodyItems): Response<ApiResponseItems>

}
