package com.dicoding.githubsocial.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubsocial.data.source.local.datastore.AppPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: AppPreferences) : ViewModel() {

    fun saveTheme(isDark: Boolean) {
        viewModelScope.launch { pref.saveThemeSetting(isDark) }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

}