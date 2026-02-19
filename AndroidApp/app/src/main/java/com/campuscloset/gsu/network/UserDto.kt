package com.campuscloset.gsu.network

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("user_id")
    val userId: Int? = null,

    val name: String,

    val email: String,

    @SerializedName("password_hash")
    val passwordHash: String,

    val role: String = "USER"
)

