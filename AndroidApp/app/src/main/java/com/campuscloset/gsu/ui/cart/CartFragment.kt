package com.campuscloset.gsu.ui.cart

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.campuscloset.gsu.R
import com.campuscloset.gsu.ui.adapters.CartAdapter
import com.campuscloset.gsu.utils.SessionManager
import com.campuscloset.gsu.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var rvCart: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private lateinit var btnClearCart: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCart = view.findViewById(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvCartTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        btnClearCart = view.findViewById(R.id.btnClearCart)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmptyCart)

        val userId = SessionManager.getUserId(requireContext())

        cartAdapter = CartAdapter { cartItem ->
            cartViewModel.removeFromCart(cartItem.cartItemId, userId)
        }
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        rvCart.adapter = cartAdapter

        cartViewModel.loadCart(userId)

        btnClearCart.setOnClickListener { cartViewModel.clearCart(userId) }

        btnCheckout.setOnClickListener {
            Toast.makeText(
                context,
                "Proceeding to checkout — ${cartViewModel.formattedTotal()}",
                Toast.LENGTH_SHORT
            ).show()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
            val isEmpty = items.isEmpty()
            tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            rvCart.visibility = if (isEmpty) View.GONE else View.VISIBLE
            btnCheckout.isEnabled = !isEmpty
            btnClearCart.isEnabled = !isEmpty
        }

        cartViewModel.cartTotal.observe(viewLifecycleOwner) { total ->
            tvTotal.text = "Total: ${"$%.2f".format(total)}"
        }

        cartViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        cartViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                cartViewModel.clearError()
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
