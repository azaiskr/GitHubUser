package com.azaiskr.githubuserprofile.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.azaiskr.githubuserprofile.data.remote.api.ApiConfig
import com.azaiskr.githubuserprofile.data.remote.response.GitHubResponse
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.domain.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserListViewModel(private val preferences: SettingPreferences) : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem?>?>()
    val listUser: MutableLiveData<List<ItemsItem?>?> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "MainViewModel"
        private const val USER_NAME = "azais"
    }

    init {
        getUserList()
    }

    internal fun getUserList() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubUser(USER_NAME)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = getErrorMessage(response)
                }
            }

            override fun onFailure(
                call: Call<GitHubResponse>,
                t: Throwable
            ) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorMessage.value = getErrorMessage(t)
            }
        })
    }

    fun getUserList(usernameSearched: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubUser(usernameSearched)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val totalItems = response.body()?.totalCount
                    if (totalItems != 0) {
                        _listUser.value = response.body()?.items
                    } else {
                        _errorMessage.value = "User not found"
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = getErrorMessage(response)
                }
            }

            override fun onFailure(
                call: Call<GitHubResponse>,
                t: Throwable
            ) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _errorMessage.value = getErrorMessage(t)
            }
        })
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

    fun getMode(): LiveData<Boolean> {
        return preferences.getMode().asLiveData()
    }

    fun setMode(mode: Boolean) {
        viewModelScope.launch { preferences.setMode(mode) }
    }

}