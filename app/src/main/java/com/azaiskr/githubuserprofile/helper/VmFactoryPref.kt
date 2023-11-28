package com.azaiskr.githubuserprofile.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azaiskr.githubuserprofile.domain.SettingPreferences
import com.azaiskr.githubuserprofile.ui.main.UserListViewModel

class VmFactoryPref(private val preferences: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java))
            return UserListViewModel(preferences) as T
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}