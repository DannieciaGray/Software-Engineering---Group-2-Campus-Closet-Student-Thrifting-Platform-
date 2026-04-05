package com.campuscloset.gsu.network

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("item_id") val itemId: Int
)