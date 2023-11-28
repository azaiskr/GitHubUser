package com.azaiskr.githubuserprofile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavUserEntity::class], version = 1, exportSchema = false)
abstract class FavUserDb : RoomDatabase() {
    abstract fun favUserDao(): FavUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavUserDb? = null

        @JvmStatic
        fun getInstance(context: Context): FavUserDb {
            if (INSTANCE == null) {
                synchronized(FavUserDb::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavUserDb::class.java, "UserDb"
                    )
                        .build()
                }
            }
            return INSTANCE as FavUserDb
        }
    }
}