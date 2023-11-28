package com.azaiskr.githubuserprofile.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favuser")
@Parcelize
data class FavUserEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,

    var username: String? = "",

    var avatarUrl: String? = null,

) : Parcelable
