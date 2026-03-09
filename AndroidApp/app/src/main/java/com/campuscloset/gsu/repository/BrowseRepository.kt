package com.campuscloset.gsu.repository

import com.campuscloset.gsu.models.Category
import com.campuscloset.gsu.models.Item
import com.campuscloset.gsu.network.SupabaseClient

class BrowseRepository {

    private val api = SupabaseClient.api

    suspend fun getItems(categoryId: Int? = null): Result<List<Item>> {
        return try {
            val filter = categoryId?.let { "eq.$it" }
            val response = api.getItems(categoryId = filter)
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun searchItems(query: String): Result<List<Item>> {
        return try {
            val response = api.getItems()
            if (response.isSuccessful) {
                val filtered = response.body()
                    ?.filter { it.title.contains(query, ignoreCase = true) }
                    ?: emptyList()
                Result.success(filtered)
            } else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = api.getCategories()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun getItemById(itemId: Int): Result<Item?> {
        return try {
            val response = api.getItemById(itemId = "eq.$itemId")
            if (response.isSuccessful) Result.success(response.body()?.firstOrNull())
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}