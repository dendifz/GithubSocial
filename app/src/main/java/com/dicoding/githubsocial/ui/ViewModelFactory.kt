package com.dicoding.githubsocial.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubsocial.data.source.local.datastore.AppPreferences
import com.dicoding.githubsocial.data.source.remote.RemoteRepository
import com.dicoding.githubsocial.di.Injection
import com.dicoding.githubsocial.ui.detailuser.DetailUserViewModel
import com.dicoding.githubsocial.ui.favorite.FavoriteViewModel
import com.dicoding.githubsocial.ui.home.HomeViewModel
import com.dicoding.githubsocial.ui.listfollow.DetailFollowViewModel
import com.dicoding.githubsocial.ui.setting.SettingViewModel
import com.dicoding.githubsocial.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val repository: RemoteRepository,
    private val pref: AppPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailUserViewModel::class.java) -> {
                DetailUserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailFollowViewModel::class.java) -> {
                DetailFollowViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.providePreferences(context)
                )
            }.also { instance = it }
    }
}