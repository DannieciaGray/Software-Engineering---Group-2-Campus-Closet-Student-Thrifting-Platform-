package com.campuscloset.gsu.network

import com.google.gson.annotations.SerializedName

data class CartItemRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("item_id") val itemId: Int,
    @SerializedName("quantity") val quantity: Int = 1
)