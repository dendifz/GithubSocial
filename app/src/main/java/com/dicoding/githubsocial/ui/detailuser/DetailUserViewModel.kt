package com.dicoding.githubsocial.ui.detailuser

import androidx.lifecycle.ViewModel
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity
import com.dicoding.githubsocial.data.source.remote.RemoteRepository

class DetailUserViewModel(private val repository: RemoteRepository) : ViewModel() {

    fun detailUser(username: String) = repository.detailUser(username)

    fun addFavorite(user: FavoriteUserEntity) = repository.insertFavoriteUser(user)

    fun cekFavorite(username: String) = repository.cekFavorite(username)

}