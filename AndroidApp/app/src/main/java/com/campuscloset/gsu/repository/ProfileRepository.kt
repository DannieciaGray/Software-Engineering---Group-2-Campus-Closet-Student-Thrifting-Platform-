package com.campuscloset.gsu.repository

import com.campuscloset.gsu.models.Item
import com.campuscloset.gsu.network.SupabaseClient
import com.campuscloset.gsu.network.UserDto

class ProfileRepository {

    private val api = SupabaseClient.api

    suspend fun getUserById(userId: Int): Result<UserDto?> {
        return try {
            val result = api.getUserById(
                idEq = "eq.$userId",
                select = "*"
            )
            Result.success(result.firstOrNull())
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getMyListings(userId: Int): Result<List<Item>> {
        return try {
            val response = api.getItems(
                status = "eq.AVAILABLE",
                select = "*,item_images(*),users(name),categories(name)",
                categoryId = null,
                order = "created_at.desc"
            )
            if (response.isSuccessful) {
                val items = response.body()
                    ?.filter { it.sellerId == userId }
                    ?: emptyList()
                Result.success(items)
            } else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}