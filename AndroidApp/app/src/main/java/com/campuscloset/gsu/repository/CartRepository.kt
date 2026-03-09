package com.campuscloset.gsu.repository

import com.campuscloset.gsu.models.CartItem
import com.campuscloset.gsu.network.CartItemRequest
import com.campuscloset.gsu.network.SupabaseClient

class CartRepository {

    private val api = SupabaseClient.api

    suspend fun getCartItems(userId: Int): Result<List<CartItem>> {
        return try {
            val response = api.getCartItems(userId = "eq.$userId")
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun addToCart(userId: Int, itemId: Int): Result<Boolean> {
        return try {
            val body = CartItemRequest(userId = userId, itemId = itemId)
            val response = api.addToCart(body)
            when {
                response.isSuccessful -> Result.success(true)
                response.code() == 409 -> Result.success(false)
                else -> Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun removeFromCart(cartItemId: Int): Result<Unit> {
        return try {
            val response = api.removeFromCart(cartItemId = "eq.$cartItemId")
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun clearCart(userId: Int): Result<Unit> {
        return try {
            val response = api.clearCart(userId = "eq.$userId")
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Error ${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}