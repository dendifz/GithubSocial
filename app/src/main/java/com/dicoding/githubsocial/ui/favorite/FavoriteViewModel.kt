package com.dicoding.githubsocial.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity
import com.dicoding.githubsocial.data.source.remote.RemoteRepository
import com.dicoding.githubsocial.util.Resource

class FavoriteViewModel(private val repository: RemoteRepository) : ViewModel() {

    fun getFavoriteUser(): LiveData<Resource<List<FavoriteUserEntity>>> =
        repository.getFavoriteList()

    fun deleteFavoriteUser(user: FavoriteUserEntity) = repository.deleteFavoriteUser(user)

}