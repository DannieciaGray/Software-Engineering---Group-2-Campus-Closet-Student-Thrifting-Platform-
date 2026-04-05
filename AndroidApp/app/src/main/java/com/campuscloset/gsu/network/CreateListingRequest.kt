package com.campuscloset.gsu.network

import com.google.gson.annotations.SerializedName

data class CreateListingRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("status") val status: String = "AVAILABLE",
    @SerializedName("seller_id") val sellerId: Int,
    @SerializedName("category_id") val categoryId: Int?,
    @SerializedName("condition") val condition: String,
    @SerializedName("size") val size: String
)