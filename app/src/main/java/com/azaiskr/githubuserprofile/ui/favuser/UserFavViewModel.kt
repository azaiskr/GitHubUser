package com.azaiskr.githubuserprofile.ui.favuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azaiskr.githubuserprofile.data.local.FavUserEntity
import com.azaiskr.githubuserprofile.data.repositories.FavUserRepository

class UserFavViewModel(application: Application) : ViewModel() {
    private val favUserRepository: FavUserRepository = FavUserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFavUser(): LiveData<List<FavUserEntity>> {
        _isLoading.value = true
        val favUser = favUserRepository.getFavUserList()
        _isLoading.value = false
        return favUser
    }
}