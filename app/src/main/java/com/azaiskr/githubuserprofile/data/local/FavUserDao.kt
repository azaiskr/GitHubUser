package com.azaiskr.githubuserprofile.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavUser(user: FavUserEntity)

    @Delete
    fun deleteFavUser(user: FavUserEntity?)

    @Query("SELECT * FROM favuser")
    fun getFavUserList(): LiveData<List<FavUserEntity>>

    @Query("SELECT * FROM favuser WHERE username = :username")
    fun getFavUser(username: String): LiveData<FavUserEntity?>

}