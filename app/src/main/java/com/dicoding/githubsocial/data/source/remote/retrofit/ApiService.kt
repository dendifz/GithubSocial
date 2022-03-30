package com.dicoding.githubsocial.data.source.remote.retrofit

import com.dicoding.githubsocial.BuildConfig
import com.dicoding.githubsocial.data.model.ListUser
import com.dicoding.githubsocial.data.model.User
import com.dicoding.githubsocial.data.source.remote.response.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers(GITHUB_API)
    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<SearchUserResponse>

    @Headers(GITHUB_API)
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<User>

    @Headers(GITHUB_API)
    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<List<ListUser>>

    @Headers(GITHUB_API)
    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<List<ListUser>>

    companion object {
        private const val GITHUB_API =
            "Authorization: token ${BuildConfig.GITHUB_TOKEN}"
    }

}