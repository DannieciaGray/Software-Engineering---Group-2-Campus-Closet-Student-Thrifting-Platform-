package com.campuscloset.gsu.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.campuscloset.gsu.models.Item
import com.campuscloset.gsu.network.UserDto
import com.campuscloset.gsu.repository.ProfileRepository
import com.campuscloset.gsu.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ProfileRepository()

    private val _user = MutableLiveData<UserDto?>()
    val user: LiveData<UserDto?> = _user

    private val _myListings = MutableLiveData<List<Item>>(emptyList())
    val myListings: LiveData<List<Item>> = _myListings

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadProfile(context: Context) {
        val userId = SessionManager.getUserId(context)
        if (userId == -1) return
        viewModelScope.launch {
            repo.getUserById(userId).onSuccess {
                _user.postValue(it)
            }.onFailure {
                _errorMessage.postValue(it.message)
            }
            repo.getMyListings(userId).onSuccess {
                _myListings.postValue(it)
            }.onFailure {
                _errorMessage.postValue(it.message)
            }
        }
    }
}