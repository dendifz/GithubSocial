package com.dicoding.githubsocial.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_user")
data class FavoriteUserEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: Int? = null,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "username")
    val username: String? = null,

    @field:ColumnInfo(name = "avatar_url")
    val avatar: String? = null,

    @field:ColumnInfo(name = "type")
    val type: String? = null,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean = false
)