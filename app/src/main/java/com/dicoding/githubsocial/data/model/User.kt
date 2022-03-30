package com.dicoding.githubsocial.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("login")
    var username: String? = null,

    @SerializedName("avatar_url")
    var avatar: String? = null,

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("company")
    var company: String? = null,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("public_repos")
    var repository: Int? = null,

    @SerializedName("followers")
    var followers: Int? = null,

    @SerializedName("following")
    var following: Int? = null
) : Parcelable