package com.dicoding.githubsocial.data.source.local.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.myDataStore by preferencesDataStore("filename")

class AppPreferences(private val context: Context) {
    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.myDataStore.edit {
            it[THEME_KEY] = isDarkModeActive
        }
    }

    fun getThemeSetting(): Flow<Boolean> =
        context.myDataStore.data.map {
            it[THEME_KEY] ?: false
        }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AppPreferences? = null
        fun getInstance(
            context: Context
        ): AppPreferences =
            instance ?: synchronized(this) {
                instance ?: AppPreferences(context)
            }.also { instance = it }
    }
}