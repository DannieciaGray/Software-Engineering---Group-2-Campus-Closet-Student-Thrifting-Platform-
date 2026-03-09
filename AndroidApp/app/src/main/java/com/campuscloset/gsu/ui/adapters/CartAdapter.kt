package com.campuscloset.gsu.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.campuscloset.gsu.R
import com.campuscloset.gsu.models.CartItem

class CartAdapter(
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_row, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivImage: ImageView = itemView.findViewById(R.id.ivCartItemImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvCartItemTitle)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        private val tvSeller: TextView = itemView.findViewById(R.id.tvCartItemSeller)
        private val btnRemove: Button = itemView.findViewById(R.id.btnRemoveFromCart)

        fun bind(cartItem: CartItem) {
            val item = cartItem.item
            tvTitle.text = item?.title ?: "Unknown Item"
            tvPrice.text = item?.formattedPrice ?: "$0.00"
            tvSeller.text = "Seller: ${item?.seller?.name ?: "Unknown"}"

            Glide.with(itemView.context)
                .load(item?.primaryImageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(ivImage)

            btnRemove.setOnClickListener { onRemove(cartItem) }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.cartItemId == newItem.cartItemId
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem == newItem
    }
}
