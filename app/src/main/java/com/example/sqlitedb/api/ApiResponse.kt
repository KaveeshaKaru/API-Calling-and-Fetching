package com.example.sqlitedb.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(

    @SerializedName("Status_Code")
    val statusCode: Int,

    @SerializedName("Message")
    val message: String?,



)
