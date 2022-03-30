package com.dicoding.githubsocial.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListUser(
    var login: String? = null,
    var avatar_url: String? = null,
    var id: Int? = null,
    var type: String? = null
) : Parcelable