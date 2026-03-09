package com.campuscloset.gsu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscloset.gsu.models.CartItem
import com.campuscloset.gsu.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _cartTotal = MutableLiveData(0.0)
    val cartTotal: LiveData<Double> = _cartTotal

    private val _cartCount = MutableLiveData(0)
    val cartCount: LiveData<Int> = _cartCount

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun loadCart(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getCartItems(userId)
                .onSuccess { items ->
                    _cartItems.value = items
                    _cartTotal.value = items.sumOf { it.subtotal }
                    _cartCount.value = items.size
                }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun addToCart(userId: Int, itemId: Int, itemTitle: String) {
        viewModelScope.launch {
            repository.addToCart(userId, itemId)
                .onSuccess { added ->
                    _toastMessage.value = if (added) "\"$itemTitle\" added to cart!"
                    else "Already in your cart"
                    loadCart(userId)
                }
                .onFailure { _errorMessage.value = it.message }
        }
    }

    fun removeFromCart(cartItemId: Int, userId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
                .onSuccess { loadCart(userId) }
                .onFailure { _errorMessage.value = it.message }
        }
    }

    fun clearCart(userId: Int) {
        viewModelScope.launch {
            repository.clearCart(userId)
                .onSuccess {
                    _cartItems.value = emptyList()
                    _cartTotal.value = 0.0
                    _cartCount.value = 0
                    _toastMessage.value = "Cart cleared"
                }
                .onFailure { _errorMessage.value = it.message }
        }
    }

    fun clearToast() { _toastMessage.value = null }
    fun clearError() { _errorMessage.value = null }

    fun formattedTotal(): String = "$%.2f".format(_cartTotal.value ?: 0.0)
}
