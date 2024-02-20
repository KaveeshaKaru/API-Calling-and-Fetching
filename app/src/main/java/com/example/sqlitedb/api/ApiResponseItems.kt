package com.example.sqlitedb.api

import com.example.sqlitedb.AlbumItem
import com.google.gson.annotations.SerializedName

data class ApiResponseItems(

    @SerializedName("Status_Code")
    val statusCode: Int,

    @SerializedName("Sync_Time")
    val syncTime: String?,

    @SerializedName("Message")
    val message: String?,

    @SerializedName("Response_Body")
    val responseBody: List<AlbumItem>?



)
