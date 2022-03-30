package com.dicoding.githubsocial.ui.listfollow

import androidx.lifecycle.ViewModel
import com.dicoding.githubsocial.data.source.remote.RemoteRepository

class DetailFollowViewModel(private val repository: RemoteRepository) : ViewModel() {

    fun getFollowersList(username: String) = repository.getFollowers(username)

    fun getFollowingList(username: String) = repository.getFollowing(username)

}