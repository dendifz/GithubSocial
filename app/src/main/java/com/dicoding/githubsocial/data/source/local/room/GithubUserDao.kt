package com.dicoding.githubsocial.data.source.local.room

import androidx.room.*
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity

@Dao
interface GithubUserDao {
    @Query("SELECT * FROM fav_user where favorite = 1")
    fun getFavoriteUser(): List<FavoriteUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: FavoriteUserEntity)

    @Delete
    fun deleteFavorite(user: FavoriteUserEntity)

    @Query("DELETE FROM fav_user WHERE favorite = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM fav_user WHERE username = :username AND favorite = 1)")
    fun isFavoriteUser(username: String): Boolean
}