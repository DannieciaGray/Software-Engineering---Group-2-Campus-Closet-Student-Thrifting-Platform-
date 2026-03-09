package com.campuscloset.gsu.ui.browse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campuscloset.gsu.LoginActivity
import com.campuscloset.gsu.R
import com.campuscloset.gsu.ui.adapters.ItemAdapter
import com.campuscloset.gsu.utils.SessionManager
import com.campuscloset.gsu.viewmodel.BrowseViewModel
import com.campuscloset.gsu.viewmodel.CartViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class BrowseFragment : Fragment() {

    private val browseViewModel: BrowseViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var rvItems: RecyclerView
    private lateinit var chipGroup: ChipGroup
    private lateinit var etSearch: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var btnLogout: Button
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_browse, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvItems = view.findViewById(R.id.rvItems)
        chipGroup = view.findViewById(R.id.chipGroupCategories)
        etSearch = view.findViewById(R.id.etSearch)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Debug log
        val userId = SessionManager.getUserId(requireContext())
        android.util.Log.d("CART_DEBUG", "userId from session = $userId")

        btnLogout.setOnClickListener {
            SessionManager.clearSession(requireContext())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        setupRecyclerView()
        setupSearch()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter(
            onItemClick = { item ->
                browseViewModel.selectItem(item)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ItemDetailFragment())
                    .addToBackStack(null)
                    .commit()
            },
            onAddToCart = { item ->
                val userId = SessionManager.getUserId(requireContext())
                android.util.Log.d("CART_DEBUG", "Adding to cart: userId=$userId itemId=${item.itemId}")
                cartViewModel.addToCart(userId, item.itemId, item.title)
            }
        )
        rvItems.layoutManager = GridLayoutManager(requireContext(), 2)
        rvItems.adapter = itemAdapter
    }

    private fun setupSearch() {
        etSearch.doAfterTextChanged { text ->
            browseViewModel.searchItems(text.toString())
        }
    }

    private fun observeViewModel() {
        browseViewModel.items.observe(viewLifecycleOwner) { items ->
            itemAdapter.submitList(items)
            tvEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            rvItems.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
        }

        browseViewModel.categories.observe(viewLifecycleOwner) { categories ->
            chipGroup.removeAllViews()
            val allChip = Chip(requireContext()).apply {
                text = "All"
                isCheckable = true
                isChecked = true
                setOnClickListener { browseViewModel.loadItems(null) }
            }
            chipGroup.addView(allChip)
            categories.forEach { category ->
                val chip = Chip(requireContext()).apply {
                    text = category.name
                    isCheckable = true
                    setOnClickListener { browseViewModel.loadItems(category.categoryId) }
                }
                chipGroup.addView(chip)
            }
        }

        browseViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        browseViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                browseViewModel.clearError()
            }
        }

        cartViewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                cartViewModel.clearToast()
            }
        }

        cartViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                android.util.Log.e("CART_DEBUG", "Cart error: $it")
                Toast.makeText(context, "Cart error: $it", Toast.LENGTH_LONG).show()
                cartViewModel.clearError()
            }
        }
    }
}