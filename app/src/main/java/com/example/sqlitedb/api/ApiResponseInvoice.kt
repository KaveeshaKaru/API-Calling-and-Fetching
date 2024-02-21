package com.example.sqlitedb.api

import com.google.gson.annotations.SerializedName

data class ApiResponseInvoice(
    @SerializedName("Status_Code")
    val statusCode: Int,

    @SerializedName("Message")
    val message: String?,

    @SerializedName("Response_Body")
    val responseBody: Any?
)
