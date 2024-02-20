package com.example.sqlitedb

import com.google.gson.annotations.SerializedName

data class AlbumItem(
    @SerializedName("Sync_Time")
    val syncTime: String?,

    @SerializedName("Code")
    val code: String?,

    @SerializedName("Name")
    val name: String?,

    @SerializedName("Printing_Name")
    val printingName: String?,

    @SerializedName("Price")
    val price: Double?,

    @SerializedName("Barcode")
    val barcode: String?,

    @SerializedName("Pack_Size")
    val packSize: String?,

    @SerializedName("Item_Group_Code")
    val itemGroupCode: String?,

    @SerializedName("Item_Group")
    val itemGroup: String?,

    @SerializedName("Order_No")
    val orderNo: Double?,

    @SerializedName("Stock_Handle")
    val stockHandle: Boolean?,

    @SerializedName("Minus_Stock")
    val minusStock: Boolean?,

    @SerializedName("Status")
    val status: Int?
)
