package com.campuscloset.gsu.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campuscloset.gsu.R
import com.campuscloset.gsu.ui.adapters.ItemAdapter
import com.campuscloset.gsu.ui.browse.ItemDetailFragment
import com.campuscloset.gsu.utils.SessionManager
import com.campuscloset.gsu.viewmodel.CartViewModel
import com.campuscloset.gsu.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter
    private lateinit var rvFavorites: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorites = view.findViewById(R.id.rvFavorites)
        tvEmpty = view.findViewById(R.id.tvEmptyFavorites)
        progressBar = view.findViewById(R.id.progressFavorites)

        // onItemClick first, onAddToCart second — matches ItemAdapter constructor
        adapter = ItemAdapter(
            onItemClick = { item ->
                val bundle = Bundle().apply { putInt("itemId", item.itemId) }
                val detailFragment = ItemDetailFragment().apply { arguments = bundle }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, detailFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onAddToCart = { item ->
                val userId = SessionManager.getUserId(requireContext())
                cartViewModel.addToCart(userId, item.itemId, item.title)
                Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
            }
        )

        rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        rvFavorites.adapter = adapter

        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            progressBar.visibility = View.GONE
            if (favorites.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                rvFavorites.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                rvFavorites.visibility = View.VISIBLE
                adapter.submitList(favorites.mapNotNull { it.item })
            }
        }

        favoritesViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        progressBar.visibility = View.VISIBLE
        favoritesViewModel.loadFavorites(requireContext())
    }
}