package com.azaiskr.githubuserprofile.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azaiskr.githubuserprofile.data.local.FavUserEntity
import com.azaiskr.githubuserprofile.data.remote.api.ApiConfig
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.data.remote.response.UserDetailResponse
import com.azaiskr.githubuserprofile.data.repositories.FavUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserDetailViewModel(application: Application) : ViewModel() {

    private val favUserRepository = FavUserRepository(application)

    private var _userData = MutableLiveData<UserDetailResponse>()
    val userData: LiveData<UserDetailResponse> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailLoading = MutableLiveData<Boolean>()
    val detailLoading: LiveData<Boolean> = _detailLoading

    private val _userFollowers = MutableLiveData<List<ItemsItem>>()
    val userFollowers: LiveData<List<ItemsItem>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _userFollowing

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun getDetailUser(username: String) {
        _detailLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>, response: Response<UserDetailResponse>
            ) {
                _detailLoading.value = false
                if (response.isSuccessful) {
                    _userData.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val errorMessage = getErrorMessage(response)
                    _errorMessage.value = errorMessage
                }
            }

            override fun onFailure(
                call: Call<UserDetailResponse>, t: Throwable
            ) {
                _detailLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                val errorMessage = getErrorMessage(t)
                _errorMessage.value = errorMessage
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val errorMessage = getErrorMessage(response)
                    _errorMessage.value = errorMessage
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                val errorMessage = getErrorMessage(t)
                _errorMessage.value = errorMessage
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val errorMessage = getErrorMessage(response)
                    _errorMessage.value = errorMessage
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                val errorMessage = getErrorMessage(t)
                _errorMessage.value = errorMessage
            }
        })
    }

    fun getFavUser(username: String): LiveData<FavUserEntity?> {
        return favUserRepository.getFavUser(username)
    }

    fun <T> getErrorMessage(response: Response<T>?): String {
        return when {
            response != null && response.code() in 400..599 -> {
                "HTTP Error: ${response.code()}"
            }

            else -> {
                "Unknown error"
            }
        }
    }

    fun getErrorMessage(t: Throwable?): String {
        return when (t) {
            is IOException -> {
                "Connection fault: ${t.message}"
            }

            else -> {
                "Unknown error: ${t?.message ?: "null"}"
            }
        }
    }

    fun addFavUser(user: FavUserEntity) {
        favUserRepository.addFavUser(user)
    }

    fun deleteFavUser(user: FavUserEntity?) {
        favUserRepository.deleteFavUser(user)
    }

    companion object {
        private const val TAG = "UserDetailViewModel"
    }

}