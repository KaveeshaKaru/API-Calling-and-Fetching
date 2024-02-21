package com.example.sqlitedb.api

import com.google.gson.annotations.SerializedName

data class ApiRequestBody2(
    @SerializedName("API_Body")
    val apiBody: List<Map<String, String>>,
    @SerializedName("Api_Action")
    val apiAction: String,
    @SerializedName("Company_Code")
    val companyCode: String,
    @SerializedName("Sync_Time")
    val syncTime: String,
    @SerializedName("Device_Id")
    val deviceId : String
)

