package com.campuscloset.gsu.repository

import com.campuscloset.gsu.models.Favorite
import com.campuscloset.gsu.network.FavoriteRequest
import com.campuscloset.gsu.network.SupabaseClient

class FavoritesRepository {

    private val api = SupabaseClient.api

    suspend fun getFavorites(userId: Int): Result<List<Favorite>> {
        return try {
            val response = api.getFavorites(
                userId = "eq.$userId",
                select = "*,items(*,item_images(*),users(name))"
            )
            if (response.isSuccessful)
                Result.success(response.body() ?: emptyList())
            else
                Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun addFavorite(userId: Int, itemId: Int): Result<Boolean> {
        return try {
            val response = api.addFavorite(FavoriteRequest(userId, itemId))
            if (response.isSuccessful || response.code() == 409)
                Result.success(true)
            else
                Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun removeFavorite(userId: Int, itemId: Int): Result<Boolean> {
        return try {
            val response = api.removeFavorite(
                userId = "eq.$userId",
                itemId = "eq.$itemId"
            )
            if (response.isSuccessful)
                Result.success(true)
            else
                Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getFavoriteItemIds(userId: Int): Result<Set<Int>> {
        return try {
            val response = api.getFavorites(
                userId = "eq.$userId",
                select = "item_id"
            )
            if (response.isSuccessful) {
                val ids = response.body()?.map { it.itemId }?.toSet() ?: emptySet()
                Result.success(ids)
            } else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}