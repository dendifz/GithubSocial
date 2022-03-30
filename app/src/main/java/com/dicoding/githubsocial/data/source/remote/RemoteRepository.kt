package com.dicoding.githubsocial.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubsocial.data.model.ListUser
import com.dicoding.githubsocial.data.model.User
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity
import com.dicoding.githubsocial.data.source.local.room.GithubUserDao
import com.dicoding.githubsocial.data.source.remote.response.SearchUserResponse
import com.dicoding.githubsocial.data.source.remote.retrofit.ApiService
import com.dicoding.githubsocial.util.AppExecutors
import com.dicoding.githubsocial.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoteRepository private constructor(
    private val apiService: ApiService,
    private val GithubUserDao: GithubUserDao,
    private val appExecutors: AppExecutors,
) {
    private var query = MutableLiveData<String>().apply { value = "dendi" }

    fun setQuery(username: String): LiveData<String> {
        query.apply { value = username }
        return query
    }

    fun checkQuery(): LiveData<String> {
        return query
    }

    fun loadSearchResult(username: String): LiveData<Resource<List<User>>> {
        val resultUser = MutableLiveData<Resource<List<User>>>()
        apiService.getSearchUser(username).enqueue(object :
            Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                resultUser.value = Resource.loading()
                if (response.isSuccessful) {
                    appExecutors.diskIO.execute {
                        response.body().let {
                            resultUser.postValue(Resource.success(response.body()?.items))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                resultUser.value = Resource.error(t.message.toString())
            }

        })
        return resultUser
    }

    fun detailUser(username: String): LiveData<Resource<User>> {
        val infoUser = MutableLiveData<Resource<User>>()
        apiService.getDetailUser(username).enqueue(object :
            Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                infoUser.value = Resource.loading()
                if (response.isSuccessful) {
                    appExecutors.diskIO.execute {
                        response.body().let {
                            infoUser.postValue(Resource.success(response.body()))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                infoUser.value = Resource.error(t.message.toString())
            }

        })

        return infoUser
    }


    fun getFollowers(username: String): LiveData<Resource<List<ListUser>>> {
        val listFollower = MutableLiveData<Resource<List<ListUser>>>()
        apiService.getFollowersUser(username).enqueue(object :
            Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                listFollower.value = Resource.loading()
                if (response.isSuccessful) {
                    appExecutors.networkIO.execute {
                        response.body().let {
                            listFollower.postValue(Resource.success(response.body()))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                listFollower.value = Resource.error(t.message.toString())
            }

        })

        return listFollower
    }

    fun getFollowing(username: String): LiveData<Resource<List<ListUser>>> {
        val listFollowing = MutableLiveData<Resource<List<ListUser>>>()
        apiService.getFollowingUser(username).enqueue(object :
            Callback<List<ListUser>> {
            override fun onResponse(
                call: Call<List<ListUser>>,
                response: Response<List<ListUser>>
            ) {
                listFollowing.value = Resource.loading()
                if (response.isSuccessful) {
                    appExecutors.networkIO.execute {
                        response.body().let {
                            listFollowing.postValue(Resource.success(response.body()))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ListUser>>, t: Throwable) {
                listFollowing.value = Resource.error(t.message.toString())
            }

        })

        return listFollowing
    }

    fun getFavoriteList(): LiveData<Resource<List<FavoriteUserEntity>>> {
        val listFavorite = MutableLiveData<Resource<List<FavoriteUserEntity>>>()
        listFavorite.postValue(Resource.loading())
        appExecutors.networkIO.execute {
            if (GithubUserDao.getFavoriteUser().isEmpty()) {
                listFavorite.postValue(Resource.error("Data Tidak Ada"))
            } else
                listFavorite.postValue(Resource.success(GithubUserDao.getFavoriteUser()))
        }
        return listFavorite
    }

    fun insertFavoriteUser(user: FavoriteUserEntity) =
        appExecutors.diskIO.execute { GithubUserDao.insertFavorite(user) }

    fun deleteFavoriteUser(user: FavoriteUserEntity) =
        appExecutors.diskIO.execute { GithubUserDao.deleteFavorite(user) }

    fun cekFavorite(username: String): Boolean {
        return GithubUserDao.isFavoriteUser(username)
    }

    companion object {
        @Volatile
        private var instance: RemoteRepository? = null
        fun getInstance(
            apiService: ApiService,
            GithubUserDao: GithubUserDao,
            appExecutors: AppExecutors
        ): RemoteRepository =
            instance ?: synchronized(this) {
                instance ?: RemoteRepository(apiService, GithubUserDao, appExecutors)
            }.also { instance = it }
    }

}