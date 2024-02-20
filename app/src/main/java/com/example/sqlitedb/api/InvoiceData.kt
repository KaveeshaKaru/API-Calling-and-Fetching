package com.example.sqlitedb.api

import com.google.gson.annotations.SerializedName

data class InvoiceData(
    @SerializedName("API_Body")
    val apiBody: List<Map<String, Any>>,
    @SerializedName("Api_Action")
    val apiAction: String,
    @SerializedName("Company_Code")
    val companyCode: String,
    @SerializedName("Sync_Time")
    val syncTime: String,
    @SerializedName("Device_Id")
    val deviceId: String? = null
)
