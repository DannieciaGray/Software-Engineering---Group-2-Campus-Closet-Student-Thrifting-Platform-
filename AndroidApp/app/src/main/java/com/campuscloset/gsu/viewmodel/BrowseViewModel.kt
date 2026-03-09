package com.campuscloset.gsu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscloset.gsu.models.Category
import com.campuscloset.gsu.models.Item
import com.campuscloset.gsu.repository.BrowseRepository
import kotlinx.coroutines.launch

class BrowseViewModel : ViewModel() {

    private val repository = BrowseRepository()

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _selectedItem = MutableLiveData<Item?>()
    val selectedItem: LiveData<Item?> = _selectedItem

    private var currentCategoryId: Int? = null

    init {
        loadCategories()
        loadItems()
    }

    fun loadItems(categoryId: Int? = null) {
        currentCategoryId = categoryId
        viewModelScope.launch {
            _isLoading.value = true
            repository.getItems(categoryId)
                .onSuccess { _items.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories()
                .onSuccess { _categories.value = it }
                .onFailure { _errorMessage.value = it.message }
        }
    }

    fun searchItems(query: String) {
        if (query.isBlank()) {
            loadItems(currentCategoryId)
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            repository.searchItems(query)
                .onSuccess { _items.value = it }
                .onFailure { _errorMessage.value = it.message }
            _isLoading.value = false
        }
    }

    fun selectItem(item: Item) {
        _selectedItem.value = item
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
