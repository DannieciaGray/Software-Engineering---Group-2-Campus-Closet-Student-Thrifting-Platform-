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
import com.campuscloset.gsu.models.Item

class ItemAdapter(
    private val onItemClick: (Item) -> Unit,
    private val onAddToCart: (Item) -> Unit
) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivImage: ImageView = itemView.findViewById(R.id.ivItemImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        private val tvCondition: TextView = itemView.findViewById(R.id.tvCondition)
        private val tvSize: TextView = itemView.findViewById(R.id.tvSize)
        private val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)

        fun bind(item: Item) {
            tvTitle.text = item.title
            tvPrice.text = item.formattedPrice
            tvCondition.text = item.condition
            tvSize.text = item.size ?: "One size"

            Glide.with(itemView.context)
                .load(item.primaryImageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(ivImage)

            itemView.setOnClickListener { onItemClick(item) }
            btnAddToCart.setOnClickListener { onAddToCart(item) }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.itemId == newItem.itemId
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }
}
