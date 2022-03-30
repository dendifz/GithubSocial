package com.dicoding.githubsocial.di

import android.content.Context
import com.dicoding.githubsocial.data.source.local.datastore.AppPreferences
import com.dicoding.githubsocial.data.source.local.room.GithubUserDatabase
import com.dicoding.githubsocial.data.source.remote.RemoteRepository
import com.dicoding.githubsocial.data.source.remote.retrofit.ApiConfig
import com.dicoding.githubsocial.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): RemoteRepository {
        val apiService = ApiConfig.getApiService()
        val database = GithubUserDatabase.getInstance(context)
        val dao = database.githubUserDao()
        val appExecutors = AppExecutors()
        return RemoteRepository.getInstance(apiService, dao, appExecutors)
    }

    fun providePreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }
}
