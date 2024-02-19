package com.example.sqlitedb.api

import com.google.gson.annotations.SerializedName

data class ApiBodyItem(
    @SerializedName("Unique_Id")
    val uniqueId: String
)