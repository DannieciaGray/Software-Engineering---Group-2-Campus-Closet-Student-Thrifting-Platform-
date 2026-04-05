package com.campuscloset.gsu.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.campuscloset.gsu.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter
    private lateinit var rvMyListings: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tvProfileName)
        tvEmail = view.findViewById(R.id.tvProfileEmail)
        rvMyListings = view.findViewById(R.id.rvMyListings)
        tvEmpty = view.findViewById(R.id.tvEmptyListings)
        progressBar = view.findViewById(R.id.progressProfile)

        val btnSellItem: Button = view.findViewById(R.id.btnSellItem)
        btnSellItem.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreateListingFragment())
                .addToBackStack(null)
                .commit()
        }

        adapter = ItemAdapter(
            onItemClick = { item ->
                profileViewModel.myListings.value
                    ?.find { it.itemId == item.itemId }
                    ?.let {
                        val bundle = Bundle().apply { putInt("itemId", item.itemId) }
                        val detailFragment = ItemDetailFragment().apply { arguments = bundle }
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
            },
            onAddToCart = { item ->
                val userId = SessionManager.getUserId(requireContext())
                cartViewModel.addToCart(userId, item.itemId, item.title)
                Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
            }
        )

        rvMyListings.layoutManager = GridLayoutManager(requireContext(), 2)
        rvMyListings.adapter = adapter

        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            tvName.text = user?.name ?: "Student"
            tvEmail.text = user?.email ?: ""
        }

        profileViewModel.myListings.observe(viewLifecycleOwner) { items ->
            progressBar.visibility = View.GONE
            if (items.isEmpty()) {
                tvEmpty.visibility = View.VISIBLE
                rvMyListings.visibility = View.GONE
            } else {
                tvEmpty.visibility = View.GONE
                rvMyListings.visibility = View.VISIBLE
                adapter.submitList(items)
            }
        }

        profileViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        progressBar.visibility = View.VISIBLE
        profileViewModel.loadProfile(requireContext())
    }
}