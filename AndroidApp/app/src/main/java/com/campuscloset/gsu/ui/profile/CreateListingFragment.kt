package com.campuscloset.gsu.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.campuscloset.gsu.R
import com.campuscloset.gsu.network.CreateListingRequest
import com.campuscloset.gsu.network.SupabaseClient
import com.campuscloset.gsu.utils.SessionManager
import com.campuscloset.gsu.viewmodel.ProfileViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateListingFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private lateinit var etTitle: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var etPrice: TextInputEditText
    private lateinit var etImageUrl: TextInputEditText
    private lateinit var spinnerCondition: Spinner
    private lateinit var spinnerSize: Spinner
    private lateinit var spinnerCategory: Spinner

    private val conditions = listOf("Like New", "Good", "Fair")
    private val sizes = listOf("XS", "S", "M", "L", "XL")
    private val categories = listOf("Tops", "Bottoms", "Dresses", "Shoes", "Accessories")
    private val categoryIds = listOf(1, 2, 3, 4, 5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.etTitle)
        etDescription = view.findViewById(R.id.etDescription)
        etPrice = view.findViewById(R.id.etPrice)
        etImageUrl = view.findViewById(R.id.etImageUrl)
        spinnerCondition = view.findViewById(R.id.spinnerCondition)
        spinnerSize = view.findViewById(R.id.spinnerSize)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)

        spinnerCondition.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, conditions)
        spinnerSize.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sizes)
        spinnerCategory.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)

        val btnSubmit: Button = view.findViewById(R.id.btnSubmitListing)
        val btnCancel: Button = view.findViewById(R.id.btnCancelListing)

        btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val imageUrl = etImageUrl.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Title is required"
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                etDescription.error = "Description is required"
                return@setOnClickListener
            }
            if (priceStr.isEmpty()) {
                etPrice.error = "Price is required"
                return@setOnClickListener
            }
            val price = priceStr.toDoubleOrNull()
            if (price == null || price <= 0) {
                etPrice.error = "Enter a valid price"
                return@setOnClickListener
            }

            val userId = SessionManager.getUserId(requireContext())
            val condition = conditions[spinnerCondition.selectedItemPosition]
            val size = sizes[spinnerSize.selectedItemPosition]
            val categoryId = categoryIds[spinnerCategory.selectedItemPosition]

            val request = CreateListingRequest(
                title = title,
                description = description,
                price = price,
                sellerId = userId,
                categoryId = categoryId,
                condition = condition,
                size = size
            )

            btnSubmit.isEnabled = false
            btnSubmit.text = "Posting..."

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = SupabaseClient.api.createListing(request)
                    if (response.isSuccessful) {
                        val newItem = response.body()?.firstOrNull()
                        if (newItem != null && imageUrl.isNotEmpty()) {
                            SupabaseClient.api.addItemImage(
                                mapOf(
                                    "item_id" to newItem.itemId,
                                    "image_url" to imageUrl,
                                    "is_primary" to true
                                )
                            )
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Listing posted!", Toast.LENGTH_SHORT).show()
                            profileViewModel.loadProfile(requireContext())
                            parentFragmentManager.popBackStack()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                            btnSubmit.isEnabled = true
                            btnSubmit.text = "Post Listing"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        btnSubmit.isEnabled = true
                        btnSubmit.text = "Post Listing"
                    }
                }
            }
        }
    }
}