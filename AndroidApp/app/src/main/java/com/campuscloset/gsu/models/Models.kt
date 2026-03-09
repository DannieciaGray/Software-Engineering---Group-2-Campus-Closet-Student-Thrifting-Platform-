package com.campuscloset.gsu.models

import com.google.gson.annotations.SerializedName

// ── Item ──────────────────────────────────────────────────────────────────────
data class Item(
    @SerializedName("item_id")     val itemId: Int = 0,
    @SerializedName("title")       val title: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("price")       val price: Double = 0.0,
    @SerializedName("status")      val status: String = "AVAILABLE",
    @SerializedName("seller_id")   val sellerId: Int = 0,
    @SerializedName("category_id") val categoryId: Int? = null,
    @SerializedName("condition")   val condition: String = "GOOD",
    @SerializedName("size")        val size: String? = null,
    @SerializedName("created_at")  val createdAt: String = "",
    // Joined via Supabase select=*,item_images(*),users(name),categories(name)
    @SerializedName("item_images") val images: List<ItemImage> = emptyList(),
    @SerializedName("users")       val seller: SellerInfo? = null,
    @SerializedName("categories")  val category: CategoryInfo? = null
) {
    val primaryImageUrl: String?
        get() = images.firstOrNull { it.isPrimary }?.imageUrl
            ?: images.firstOrNull()?.imageUrl

    val formattedPrice: String
        get() = "$%.2f".format(price)
}

// ── ItemImage ─────────────────────────────────────────────────────────────────
data class ItemImage(
    @SerializedName("image_id")   val imageId: Int = 0,
    @SerializedName("item_id")    val itemId: Int = 0,
    @SerializedName("image_url")  val imageUrl: String = "",
    @SerializedName("is_primary") val isPrimary: Boolean = false
)

// ── Slim joined objects ───────────────────────────────────────────────────────
data class SellerInfo(
    @SerializedName("name") val name: String = ""
)

data class CategoryInfo(
    @SerializedName("name") val name: String = ""
)

// ── Category ──────────────────────────────────────────────────────────────────
data class Category(
    @SerializedName("category_id") val categoryId: Int = 0,
    @SerializedName("name")        val name: String = "",
    @SerializedName("description") val description: String? = null
)

// ── CartItem ──────────────────────────────────────────────────────────────────
data class CartItem(
    @SerializedName("cart_item_id") val cartItemId: Int = 0,
    @SerializedName("user_id")      val userId: Int = 0,
    @SerializedName("item_id")      val itemId: Int = 0,
    @SerializedName("quantity")     val quantity: Int = 1,
    @SerializedName("added_at")     val addedAt: String = "",
    @SerializedName("items")        val item: Item? = null
) {
    val subtotal: Double get() = (item?.price ?: 0.0) * quantity
}

// ── Favorite ──────────────────────────────────────────────────────────────────
data class Favorite(
    @SerializedName("favorite_id") val favoriteId: Int = 0,
    @SerializedName("user_id")     val userId: Int = 0,
    @SerializedName("item_id")     val itemId: Int = 0,
    @SerializedName("saved_at")    val savedAt: String = "",
    @SerializedName("items")       val item: Item? = null
)
