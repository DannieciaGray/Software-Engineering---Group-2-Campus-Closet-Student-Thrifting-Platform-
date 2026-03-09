package com.campuscloset.gsu.network

import com.google.gson.annotations.SerializedName
import com.campuscloset.gsu.models.CartItem
import com.campuscloset.gsu.models.Category
import com.campuscloset.gsu.models.Favorite
import com.campuscloset.gsu.models.Item
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApi {

    // ── EXISTING Sprint 2 methods ─────────────────────────────────────────────

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
    suspend fun insertUser(@Body user: UserDto): List<UserDto>

    @GET("users")
    suspend fun getUserById(
        @Query("user_id") idEq: String,
        @Query("select") select: String = "*"
    ): List<UserDto>

    // ── Sprint 3: Items (Browse) ──────────────────────────────────────────────

    @GET("items")
    suspend fun getItems(
        @Query("status") status: String = "eq.AVAILABLE",
        @Query("select") select: String = "*,item_images(*),users(name),categories(name)",
        @Query("category_id") categoryId: String? = null,
        @Query("order") order: String = "created_at.desc"
    ): Response<List<Item>>

    @GET("items")
    suspend fun getItemById(
        @Query("item_id") itemId: String,
        @Query("select") select: String = "*,item_images(*),users(name),categories(name)"
    ): Response<List<Item>>

    // ── Sprint 3: Categories ──────────────────────────────────────────────────

    @GET("categories")
    suspend fun getCategories(
        @Query("select") select: String = "*"
    ): Response<List<Category>>

    // ── Sprint 3: Cart ────────────────────────────────────────────────────────

    @GET("cart_items")
    suspend fun getCartItems(
        @Query("user_id") userId: String,
        @Query("select") select: String = "*,items(*,item_images(*),users(name))"
    ): Response<List<CartItem>>

    @Headers("Prefer: return=representation")
    @POST("cart_items")
    suspend fun addToCart(@Body body: CartItemRequest): Response<List<CartItem>>

    @DELETE("cart_items")
    suspend fun removeFromCart(@Query("cart_item_id") cartItemId: String): Response<Unit>

    @DELETE("cart_items")
    suspend fun clearCart(@Query("user_id") userId: String): Response<Unit>

    // ── Sprint 3: Favorites ───────────────────────────────────────────────────

    @GET("favorites")
    suspend fun getFavorites(
        @Query("user_id") userId: String,
        @Query("select") select: String = "*,items(*,item_images(*))"
    ): Response<List<Favorite>>

    @Headers("Prefer: return=representation")
    @POST("favorites")
    suspend fun addFavorite(@Body body: Map<String, Any>): Response<List<Favorite>>

    @DELETE("favorites")
    suspend fun removeFavorite(
        @Query("user_id") userId: String,
        @Query("item_id") itemId: String
    ): Response<Unit>

}
