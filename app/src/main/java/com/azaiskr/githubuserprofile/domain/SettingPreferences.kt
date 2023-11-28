package com.azaiskr.githubuserprofile.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val MODE_KEY = booleanPreferencesKey("mode")

    fun getMode(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[MODE_KEY] ?: false
        }
    }

    suspend fun setMode(mode: Boolean) {
        dataStore.edit { preferences ->
            preferences[MODE_KEY] = mode
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingPreferences(dataStore)
            }
        }
    }
}