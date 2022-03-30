package com.dicoding.githubsocial.ui.home

import androidx.lifecycle.ViewModel
import com.dicoding.githubsocial.data.source.remote.RemoteRepository

class HomeViewModel(private val repository: RemoteRepository) : ViewModel() {

    fun setQuery(username: String) = repository.setQuery(username)

    fun checkUser() = repository.checkQuery()

    fun getListUser(username: String) = repository.loadSearchResult(username)

}
