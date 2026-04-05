package com.campuscloset.gsu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.campuscloset.gsu.models.Favorite
import com.campuscloset.gsu.repository.FavoritesRepository
import com.campuscloset.gsu.utils.SessionManager
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = FavoritesRepository()

    private val _favorites = MutableLiveData<List<Favorite>>(emptyList())
    val favorites: LiveData<List<Favorite>> = _favorites

    private val _favoriteItemIds = MutableLiveData<Set<Int>>(emptySet())
    val favoriteItemIds: LiveData<Set<Int>> = _favoriteItemIds

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadFavorites(context: android.content.Context) {
        val userId = SessionManager.getUserId(context)
        if (userId == -1) return
        viewModelScope.launch {
            repo.getFavorites(userId).onSuccess {
                _favorites.postValue(it)
                _favoriteItemIds.postValue(it.map { f -> f.itemId }.toSet())
            }.onFailure {
                _errorMessage.postValue(it.message)
            }
        }
    }

    fun toggleFavorite(context: android.content.Context, itemId: Int) {
        val userId = SessionManager.getUserId(context)
        if (userId == -1) return
        viewModelScope.launch {
            val currentIds = _favoriteItemIds.value ?: emptySet()
            if (currentIds.contains(itemId)) {
                repo.removeFavorite(userId, itemId).onSuccess {
                    _favoriteItemIds.postValue(currentIds - itemId)
                    _favorites.postValue(_favorites.value?.filter { it.itemId != itemId })
                }
            } else {
                repo.addFavorite(userId, itemId).onSuccess {
                    _favoriteItemIds.postValue(currentIds + itemId)
                    loadFavorites(context)
                }
            }
        }
    }
}