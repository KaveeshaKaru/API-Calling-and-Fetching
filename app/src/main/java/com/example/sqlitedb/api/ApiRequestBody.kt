package com.example.sqlitedb.api

import com.google.gson.annotations.SerializedName

data class ApiRequestBody(
    @SerializedName("API_Body")
    val apiBody: List<ApiBodyItem>,

    @SerializedName("Api_Action")
    val apiAction: String,

    @SerializedName("Sync_Time")
    val syncTime: String,

    @SerializedName("Company_Code")
    val companyCode: String
)
