package com.campuscloset.gsu.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.campuscloset.gsu.R
import com.campuscloset.gsu.utils.SessionManager
import com.campuscloset.gsu.viewmodel.BrowseViewModel
import com.campuscloset.gsu.viewmodel.CartViewModel
import com.campuscloset.gsu.viewmodel.FavoritesViewModel

class ItemDetailFragment : Fragment() {

    private val browseViewModel: BrowseViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_item_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivImage: ImageView = view.findViewById(R.id.ivDetailImage)
        val tvTitle: TextView = view.findViewById(R.id.tvDetailTitle)
        val tvPrice: TextView = view.findViewById(R.id.tvDetailPrice)
        val tvSeller: TextView = view.findViewById(R.id.tvDetailSeller)
        val tvCondition: TextView = view.findViewById(R.id.tvDetailCondition)
        val tvSize: TextView = view.findViewById(R.id.tvDetailSize)
        val tvCategory: TextView = view.findViewById(R.id.tvDetailCategory)
        val tvDescription: TextView = view.findViewById(R.id.tvDetailDescription)
        val btnAddToCart: Button = view.findViewById(R.id.btnDetailAddToCart)
        val btnSaveToFavorites: Button = view.findViewById(R.id.btnSaveToFavorites)
        val btnBack: ImageButton = view.findViewById(R.id.btnBack)

        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        browseViewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            item ?: return@observe

            tvTitle.text = item.title
            tvPrice.text = item.formattedPrice
            tvSeller.text = "Sold by: ${item.seller?.name ?: "Unknown"}"
            tvCondition.text = "Condition: ${item.condition}"
            tvSize.text = "Size: ${item.size ?: "One size"}"
            tvCategory.text = "Category: ${item.category?.name ?: "—"}"
            tvDescription.text = item.description

            Glide.with(this)
                .load(item.primaryImageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(ivImage)

            btnAddToCart.setOnClickListener {
                val userId = SessionManager.getUserId(requireContext())
                cartViewModel.addToCart(userId, item.itemId, item.title)
            }

            btnSaveToFavorites.setOnClickListener {
                favoritesViewModel.toggleFavorite(requireContext(), item.itemId)
                Toast.makeText(requireContext(), "Saved to Favorites! ♡", Toast.LENGTH_SHORT).show()
            }
        }

        cartViewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                cartViewModel.clearToast()
            }
        }
    }
}