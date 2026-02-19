package com.campuscloset.gsu.network

import retrofit2.http.*

interface SupabaseApi {

    @GET("users")
    suspend fun getUsersByEmail(
        @Query("email") emailEq: String,
        @Query("select") select: String = "*"
    ): List<UserDto>

    @GET("users")
    suspend fun login(
        @Query("email") emailEq: String,
        @Query("password_hash") passEq: String,
        @Query("select") select: String = "*"
    ): List<UserDto>

    @Headers("Prefer: return=representation")
    @POST("users")
    suspend fun insertUser(
        @Body user: UserDto
    ): List<UserDto>

    @GET("users")
    suspend fun getUserById(
        @Query("user_id") idEq: String,
        @Query("select") select: String = "*"
    ): List<UserDto>

}




